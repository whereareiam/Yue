package com.aeritt.yue.api.util;

public interface BeanRegistrationUtil {
	void registerSingleton(String beanName, Class<?> beanClass, Object beanInstance);

	void destroyBean(String name);
}
