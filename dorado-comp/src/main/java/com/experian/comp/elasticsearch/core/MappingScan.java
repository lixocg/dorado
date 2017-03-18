package com.experian.comp.elasticsearch.core;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import com.experian.comp.elasticsearch.annotation.Document;
import com.experian.comp.elasticsearch.annotation.Field;
import com.experian.comp.elasticsearch.annotation.Nested;
import com.experian.comp.elasticsearch.annotation.NestedType;
import com.experian.comp.elasticsearch.enums.FieldType;
import com.experian.comp.elasticsearch.modle.Mapping;
import com.google.common.collect.Maps;

/**
 * 实体elasticsearch Mapping映射
 * 
 * @author lixiongcheng
 *
 */
public class MappingScan {
	protected final Log logger = LogFactory.getLog(getClass());

	private static final String RESOURCE_PATTERN = "/**/*.class";

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private List<String> packagesList = new LinkedList<String>();

	private List<TypeFilter> typeFilters = new LinkedList<TypeFilter>();

	private Set<Class<?>> classSet = new HashSet<Class<?>>();

	/**
	 * 构造函数
	 * 
	 * @param packagesToScan
	 *            指定哪些包需要被扫描,支持多个包"package.a,package.b"并对每个包都会递归搜索
	 * @param annotationFilter
	 *            指定扫描包中含有特定注解标记的bean,支持多个注解
	 */
	public MappingScan(String[] packagesToScan) {
		if (packagesToScan != null) {
			for (String packagePath : packagesToScan) {
				this.packagesList.add(packagePath);
			}
			// 加载文档参数
			scanMapping();
		}
	}

	private void scanMapping() {
		typeFilters.add(new AnnotationTypeFilter(Document.class, false));
		typeFilters.add(new AnnotationTypeFilter(Nested.class, false));
		Set<Class<?>> set = null;
		try {
			set = getClassSet();
			Iterator<Class<?>> it = set.iterator();
			while (it.hasNext()) {
				Class<?> clazz = it.next();
				if (!clazz.isAnnotationPresent(Document.class)) {
					continue;
				}
				Document doc = clazz.getAnnotation(Document.class);
				Mapping _mapping = new Mapping();
				Map<String, Map<String, Map<String, Map<String, Map<String, Object>>>>> mapping = Maps.newHashMap();
				Map<String, Map<String, Map<String, Map<String, Object>>>> v1 = Maps.newHashMap();
				Map<String, Map<String, Map<String, Object>>> v2 = Maps.newHashMap();
				Map<String, Map<String, Object>> v3 = Maps.newHashMap();
				v2.put("properties", v3);
				v1.put(doc.type(), v2);
				mapping.put("mappings", v1);
				_mapping.setMapping(mapping);
				_mapping.setRawClass(clazz);

				java.lang.reflect.Field[] fields = clazz.getDeclaredFields();

				if (fields != null && fields.length != 0) {

					for (java.lang.reflect.Field field : fields) {
						Field fieldAnnotation = field.getAnnotation(Field.class);
						Map<String, Object> v4 = Maps.newHashMap();
						if (fieldAnnotation.type().equals(FieldType.Nested)) {
							NestedType nestedTypeAnnotation = field.getAnnotation(NestedType.class);
							if (nestedTypeAnnotation == null) {
								throw new RuntimeException(
										String.format("内嵌属性[%s]未指明内嵌类，用@NestedType注解标识", field.getName()));
							}
							Class<?> nestedClass = nestedTypeAnnotation.clazz();
							java.lang.reflect.Field[] nestedFields = nestedClass.getDeclaredFields();
							Map<String, Object> nestedVal1 = Maps.newHashMap();
							Map<String, Object> nestedVal2 = Maps.newHashMap();
							nestedVal1.put("properties", nestedVal2);
							nestedVal1.put("type", fieldAnnotation.type().toString().toLowerCase());
							v3.put(field.getName(), nestedVal1);
							for (java.lang.reflect.Field nestedField : nestedFields) {
								Field nestedFieldAnnotation = nestedField.getAnnotation(Field.class);
								Map<String, Object> nestedVal3 = Maps.newHashMap();
								nestedVal3.put("type", nestedFieldAnnotation.type().toString().toLowerCase());
								nestedVal3.put("store", nestedFieldAnnotation.store());
								nestedVal3.put("index", nestedFieldAnnotation.index());
								if(nestedFieldAnnotation.type().equals(FieldType.Date)){
									nestedVal3.put("format",nestedFieldAnnotation.format());
								}
								nestedVal2.put(nestedField.getName(), nestedVal3);
							}

						} else {
							v4.put("type", fieldAnnotation.type().toString().toLowerCase());
							v4.put("store", fieldAnnotation.store());
							v4.put("index", fieldAnnotation.index());
							if(fieldAnnotation.type().equals(FieldType.Date)){
								v4.put("format",fieldAnnotation.format());
							}
							v3.put(field.getName(), v4);
						}
					}
				}
				MappingHolder.getInstance().register(_mapping);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将符合条件的Bean以Class集合的形式返回
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Set<Class<?>> getClassSet() throws IOException, ClassNotFoundException {
		this.classSet.clear();
		if (!this.packagesList.isEmpty()) {
			for (String pkg : this.packagesList) {
				String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
						+ ClassUtils.convertClassNameToResourcePath(pkg) + RESOURCE_PATTERN;
				Resource[] resources = this.resourcePatternResolver.getResources(pattern);
				MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
				for (Resource resource : resources) {
					if (resource.isReadable()) {
						MetadataReader reader = readerFactory.getMetadataReader(resource);
						String className = reader.getClassMetadata().getClassName();
						if (matchesEntityTypeFilter(reader, readerFactory)) {
							this.classSet.add(Class.forName(className));
						}
					}
				}
			}
		}
		// 输出日志
		if (logger.isInfoEnabled()) {
			for (Class<?> clazz : this.classSet) {
				logger.info(String.format("Found class:%s", clazz.getName()));
			}
		}
		return this.classSet;
	}

	/**
	 * 检查当前扫描到的Bean含有任何一个指定的注解标记
	 * 
	 * @param reader
	 * @param readerFactory
	 * @return
	 * @throws IOException
	 */
	private boolean matchesEntityTypeFilter(MetadataReader reader, MetadataReaderFactory readerFactory)
			throws IOException {
		if (!this.typeFilters.isEmpty()) {
			for (TypeFilter filter : this.typeFilters) {
				if (filter.match(reader, readerFactory)) {
					return true;
				}
			}
		}
		return false;
	}
}
