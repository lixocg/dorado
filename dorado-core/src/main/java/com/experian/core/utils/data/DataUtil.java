/** 
* Project Name:dorado-core 
* File Name:DataTransfer.java 
* Package Name:com.experian.core.utils.data 
* Date:2016年9月2日上午10:43:42 
* Copyright (c) 2016
* 
*/

package com.experian.core.utils.data;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

/**
 * ClassName:DataTransfer <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date:2016年9月2日 上午10:43:42 <br/>
 * 
 * @author e00898a
 * @version
 * @see
 */
public class DataUtil {
	public static void beanReplace(Object bean) {
		PropertyDescriptor[] props = null;
		try {
			props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		if (props != null) {
			for (int i = 0; i < props.length; i++) {
				try {
					String key = props[i].getName();
					doReplace(bean,key,props[i].getReadMethod().invoke(bean));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void arrayReplace(Object[] obj) {
		if(obj==null){
			return;
		}
		for (int i=0;i<obj.length;i++) {
			if(isIgnore(obj[i])){
				continue;
			}
			if(obj[i] instanceof String){
				obj[i]=replaceSpecialChar((String)obj[i]);
				continue;
			}
			beanReplace(obj[i]);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setReplace(Set set) {
		if (set != null && set.size() > 0) {
			Iterator it = set.iterator();
			List list=new ArrayList();
	        while (it.hasNext()) {
	            // it.next()返回的是一个对象类型，需要强制转换  
	            Object obj = it.next(); 
	            if(isIgnore(obj)){
	            	continue;
				}
				if(obj instanceof String){
					it.remove();
					list.add(replaceSpecialChar((String)obj));
					continue;
				}
				beanReplace(obj);
	        }  
	        if(list.size()!=0){
	        	set.addAll(list);
	        }
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void mapReplace(Map map) {
		if (map != null) {
			Map<Object,Object> m=map;
			for(Map.Entry<Object, Object> entry : m.entrySet()){
				Object key=entry.getKey();
				Object val=entry.getValue();
	            if(isIgnore(val)){
					continue;
				}
				if(val instanceof String){
					map.put(key, replaceSpecialChar((String)val));
					continue;
				}
				beanReplace(map.get(key));
	        }
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void listReplace(List list) {
		if (list != null && list.size() != 0) {
			for (int i=0;i<list.size();i++) {
				if(isIgnore(list.get(i))){
					continue;
				}
				if(list.get(i) instanceof String){
					list.set(i,replaceSpecialChar((String)list.get(i)));
					continue;
				}
				beanReplace(list.get(i));
			}
		}
	}
	private static void doReplace(Object bean, String name, Object value) {
		try {
			if (isIgnore(value)) {
				return;
			} else if (value instanceof String) {
				BeanUtils.setProperty(bean, name, replaceSpecialChar((String)value));
			} else if (value instanceof Object[]) {
				arrayReplace((Object[]) value);
			} else if (value instanceof List) {
				listReplace((List<?>) value);
			} else if (value instanceof Map) {
				mapReplace((Map<?, ?>) value);
			} else if (value instanceof Set) {
				setReplace((Set<?>) value);
			} else {
				beanReplace(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	    /** 
	     * replaceSpecialChar:(). <br/> 
	     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
	     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
	     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
	     * 
	     * @author e00898a 
	     * @param str
	     * @return 
	     */  
	private static String replaceSpecialChar(String str) {
		return str.replace("N", "(n)");
	}
	    /** 
	     * isIgnore:(忽略的属性类型). <br/> 
	     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
	     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
	     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
	     * 
	     * @author e00898a 
	     * @param obj
	     * @return 
	     */  
	private static boolean isIgnore(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof Integer) {
			return true;
		}
		if (obj instanceof Double) {
			return true;
		}
		if (obj instanceof Float) {
			return true;
		}
		if (obj instanceof Long) {
			return true;
		}
		if (obj instanceof Short) {
			return true;
		}
		if (obj instanceof Boolean) {
			return true;
		}
		if (obj instanceof Byte) {
			return true;
		}
		if (obj instanceof Character) {
			return true;
		}
		if (obj instanceof Enum) {
			return true;
		}
		return false;
	}
}
