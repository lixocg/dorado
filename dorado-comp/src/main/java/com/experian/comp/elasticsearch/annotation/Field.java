package com.experian.comp.elasticsearch.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.experian.comp.elasticsearch.enums.FieldType;

/**
 * 实体字段注解
 * 
 * @author lixiongcheng
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface Field {

	FieldType type() default FieldType.Auto;

	FieldIndex index() default FieldIndex.analyzed;

	String format() default "yyyy-MM-dd HH:mm:ss";

	String pattern() default "";

	boolean store() default false;

	String searchAnalyzer() default "";

	String analyzer() default "";

	String[] ignoreFields() default {};

	boolean includeInParent() default false;
}