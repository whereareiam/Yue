package com.aeritt.yue.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class BeanRegistrationUtil implements com.aeritt.yue.api.util.BeanRegistrationUtil {
	private static ConfigurableApplicationContext context;

	@Autowired
	public BeanRegistrationUtil(ConfigurableApplicationContext context) {
		BeanRegistrationUtil.context = context;
	}

	public void registerSingleton(String beanName, Class<?> beanClass, Object beanInstance) {
		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
		beanDefinition.setLazyInit(true);
		beanDefinition.setAutowireCandidate(true);

		context.getBeanFactory().registerSingleton(beanName, beanInstance);
	}

	public void destroyBean(String name) {
		context.getBeanFactory().destroyBean(name);
	}
}

