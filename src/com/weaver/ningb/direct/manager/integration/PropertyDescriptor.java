package com.weaver.ningb.direct.manager.integration;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;

/**
 * 对象属性
 * 
 * @author liberal
 *
 */
public class PropertyDescriptor {
	
	private String name;			// 属性名称, 去掉 get, is, set, 首字母转为小写
	private Method readMethod;		// 读方法, JavaBean(getXxx, isXx), 其他
	private Method writeMethod;		// 写方法, JavaBean(setXxx)
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Method getReadMethod() {
		return readMethod;
	}

	public void setReadMethod(Method readMethod) {
		this.readMethod = readMethod;
	}

	public Method getWriteMethod() {
		return writeMethod;
	}

	public void setWriteMethod(Method writeMethod) {
		this.writeMethod = writeMethod;
	}

	/**
     * Returns the Java type info for the property.
     * Note that the {@code Class} object may describe
     * primitive Java types such as {@code int}.
     * This type is returned by the read method
     * or is used as the parameter type of the write method.
     * Returns {@code null} if the type is an indexed property
     * that does not support non-indexed access.
     * @return the {@code Class} object that represents the Java type info,
     *         or {@code null} if the type cannot be determined
     */
	public Class<?> getPropertyType() {
		Class<?> propertyType = null;
		try {
			if (readMethod != null) {
                propertyType = getReturnType(readMethod.getDeclaringClass(), readMethod);
                if (propertyType == Void.TYPE) {
                    throw new IntrospectionException("read method " +
                                        readMethod.getName() + " returns void");
                }
            }
			if (writeMethod != null) {
                Class<?> params[] = getParameterTypes(writeMethod.getDeclaringClass(), writeMethod);
                if (params.length != 1) {
                    throw new IntrospectionException("bad write method arg count: "
                                                     + writeMethod);
                }
                if (propertyType != null && !params[0].isAssignableFrom(propertyType)) {
                    throw new IntrospectionException("type mismatch between read and write methods");
                }
                propertyType = params[0];
            }
		} catch (IntrospectionException e) {
			// 
		}
		return propertyType;
	}
	
	/**
     * Resolves the return type of the method.
     * @param base the class that contains the method in the hierarchy
     * @param method the object that represents the method
     * @return a class identifying the return type of the method
     * @see Method#getGenericReturnType
     * @see Method#getReturnType
     */
    private Class<?> getReturnType(Class<?> base, Method method) {
        if (base == null) {
            base = method.getDeclaringClass();
        }
        return method.getReturnType();
    }
    
    /**
     * Resolves the parameter types of the method.
     * @param base the class that contains the method in the hierarchy
     * @param method the object that represents the method
     * @return an array of classes identifying the parameter types of the method
     * @see Method#getGenericParameterTypes
     * @see Method#getParameterTypes
     */
    private Class<?>[] getParameterTypes(Class<?> base, Method method) {
        if (base == null) {
            base = method.getDeclaringClass();
        }
        return method.getParameterTypes();
    }

}
