package com.java.xdd.shiro.authz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPermission {
	/**
	 * 数据库表名称
	 * @return
	 */
	String tableName();

	/**
	 * 字段名称
	 * @return
	 */
	String[] fieldNames();

	/**
	 * 字段值
	 * @return
	 */
	String[] fieldValues();

	/**
	 * 查询的字段
	 * @return
	 */
	String[] queryFields();

	/**
	 * 查询条件
	 * @return
	 */
	String[] queryParams();

	/**
	 * 是否是登陆人的
	 * @return
	 */
	boolean isLoginUser();

}
