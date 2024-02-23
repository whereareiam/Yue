package me.whereareiam.yue.core.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class BeanRegistrationUtil {
	private static ConfigurableApplicationContext context;

	@Autowired
	public BeanRegistrationUtil(ConfigurableApplicationContext context) {
		BeanRegistrationUtil.context = context;
	}

	public static void registerSingleton(String beanName, Class<?> beanClass, Object beanInstance) {
		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
		beanDefinition.setLazyInit(true);
		beanDefinition.setAutowireCandidate(true);

		context.getBeanFactory().registerSingleton(beanName, beanInstance);
	}

	public static void destroyBean(String name) {
		context.getBeanFactory().destroyBean(name);
	}
}

