package com.weaver.ningb.direct.manager.integration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import com.weaver.ningb.logging.LogFactory;

/**
 * 对象辅助
 * 
 * @author liberal
 *
 */
public class BeanWrapper {
	
	private static final Log logger = LogFactory.getLog(BeanWrapper.class);
	
	
	private Object object;						// 对象
	private Map<String, PropertyDescriptor> propertyDescriptorMap;	
												// 属性描述符: key.name
	
	
	public BeanWrapper(Object object) {
		this.object = object;
		init();
	}
	
	
	/**
	 * 初始化
	 */
	public void init() {
		try {
			Class<?> clazz = this.object.getClass();
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				String baseName = method.getName();
				
				// getXxx
				if (baseName.indexOf("get") == 0) {
					String name = toLowerCaseFirstOne(baseName.substring("get".length()));
					
					if (StringUtils.isBlank(name)) continue;
					if ("class".equals(name)) continue;		// 过滤掉 getClass()

					PropertyDescriptor propertyDescriptor = getPropertyDescriptorMap().get(name);
					if (propertyDescriptor == null) {
						propertyDescriptor = new PropertyDescriptor();
						propertyDescriptor.setName(name);
						propertyDescriptor.setReadMethod(method);
					} else {
						propertyDescriptor.setReadMethod(method);
					}
					
					getPropertyDescriptorMap().put(name, propertyDescriptor);
				}
				
				// isXxx
				if (baseName.indexOf("is") == 0) {
					String name = toLowerCaseFirstOne(baseName.substring("is".length()));
					
					if (StringUtils.isBlank(name)) continue;

					PropertyDescriptor propertyDescriptor = getPropertyDescriptorMap().get(name);
					if (propertyDescriptor == null) {
						propertyDescriptor = new PropertyDescriptor();
						propertyDescriptor.setName(name);
						propertyDescriptor.setReadMethod(method);
					} else {
						propertyDescriptor.setReadMethod(method);
					}
					
					getPropertyDescriptorMap().put(name, propertyDescriptor);
				}
				
				// setXxx
				if (baseName.indexOf("set") == 0) {
					String name = toLowerCaseFirstOne(baseName.substring("set".length()));
					
					if (StringUtils.isBlank(name)) continue;

					PropertyDescriptor propertyDescriptor = getPropertyDescriptorMap().get(name);
					if (propertyDescriptor == null) {
						propertyDescriptor = new PropertyDescriptor();
						propertyDescriptor.setName(name);
						propertyDescriptor.setWriteMethod(method);
					} else {
						propertyDescriptor.setWriteMethod(method);
					}
					
					getPropertyDescriptorMap().put(name, propertyDescriptor);
				}
				
			}
		} catch (Exception e) {
			logger.error("Error init", e);
		}
	}
	
	
	/**
	 * Determine whether the specified property is readable.
	 * <p>Returns {@code false} if the property doesn't exist.
	 * @param propertyName the property to check
	 * (may be a nested path and/or an indexed/mapped property)
	 * @return whether the property is readable
	 */
	public boolean isReadableProperty(String propertyName) {
		PropertyDescriptor propertyDescriptor = getPropertyDescriptorMap().get(propertyName);
		if (propertyDescriptor == null) return false;
		return propertyDescriptor.getReadMethod() != null;
	}
	
	/**
	 * Determine whether the specified property is writable.
	 * <p>Returns {@code false} if the property doesn't exist.
	 * @param propertyName the property to check
	 * (may be a nested path and/or an indexed/mapped property)
	 * @return whether the property is writable
	 */
	public boolean isWritableProperty(String propertyName) {
		PropertyDescriptor propertyDescriptor = getPropertyDescriptorMap().get(propertyName);
		if (propertyDescriptor == null) return false;
		return propertyDescriptor.getWriteMethod() != null;
	}
	
	/**
	 * Get the current value of the specified property.
	 * @param propertyName the name of the property to get the value of
	 * (may be a nested path and/or an indexed/mapped property)
	 * @return the value of the property
	 */
	public Object getPropertyValue(String propertyName) {
		Object result = null;
		try {
			PropertyDescriptor propertyDescriptor = getPropertyDescriptorMap().get(propertyName);
			if (propertyDescriptor == null) return result;
			Method readMethod = propertyDescriptor.getReadMethod();
			if (readMethod == null) return result;
			result = readMethod.invoke(this.object);
		} catch (IllegalAccessException e) {
			logger.error("IllegalAccessException:", e);
		} catch (IllegalArgumentException e) {
			logger.error("IllegalArgumentException:", e);
		} catch (InvocationTargetException e) {
			logger.error("InvocationTargetException:", e);
		}
		return result;
	}
	
	/**
	 * Determine the property type for the specified property,
	 * either checking the property descriptor or checking the value
	 * in case of an indexed or mapped element.
	 * @param propertyName the property to check
	 * (may be a nested path and/or an indexed/mapped property)
	 * @return the property type for the particular property,
	 * or {@code null} if not determinable
	 */
	public Class<?> getPropertyType(String propertyName) {
		PropertyDescriptor propertyDescriptor = getPropertyDescriptorMap().get(propertyName);
		if (propertyDescriptor == null) return null;
		return propertyDescriptor.getPropertyType();
	}
	
	public Map<String, PropertyDescriptor> getPropertyDescriptorMap() {
		if (propertyDescriptorMap == null) {
			propertyDescriptorMap = new HashMap<String, PropertyDescriptor>();
		}
		return propertyDescriptorMap;
	}
	
	
	/**
	 * 将字符串首字母转换为小写
	 * 
	 * @param str
	 * 					字符串信息
	 * @return 转换后的字符串信息
	 */
	private String toLowerCaseFirstOne(String str){
		if (StringUtils.isBlank(str)) return str;
		if (Character.isLowerCase(str.charAt(0)))
			return str;
		else
			return (new StringBuilder())
					.append(Character.toLowerCase(str.charAt(0)))
					.append(str.substring(1)).toString();
	}

}
