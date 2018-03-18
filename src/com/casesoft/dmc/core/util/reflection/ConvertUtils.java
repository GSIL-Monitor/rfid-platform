package com.casesoft.dmc.core.util.reflection;

import java.util.Date;

public class ConvertUtils {

	/**
	 * 转换字符串到相应类型.
	 * 
	 * @param value 待转换的字符串.
	 * @param toType 转换目标类型.
	 */
	static {
		org.apache.commons.beanutils.ConvertUtils.register(new UtilDateConverter(), Date.class);
	}
	
	public static Object convertStringToObject(String value, Class<?> toType) {
		try {
			return org.apache.commons.beanutils.ConvertUtils.convert(value, toType);
		} catch (Exception e) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * winston 新增
	 * 转化String数组类型到对应Object[]
	 * @param values
	 * @param propertyClass
	 * @return
	 */
	public static Object convertStringToObject(String[] values,
			Class<?> propertyClass) {
		try {
			return org.apache.commons.beanutils.ConvertUtils.convert(values, propertyClass);
		} catch (Exception e) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}
		
	}
}
