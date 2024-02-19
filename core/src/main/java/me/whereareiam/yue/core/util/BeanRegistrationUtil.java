package me.whereareiam.yue.core.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanRegistrationUtil {

	private final ConfigurableApplicationContext context;

	public BeanRegistrationUtil(ConfigurableApplicationContext context) {
		this.context = context;
	}

	public void registerSingleton(String beanName, Class<?> beanClass, Object beanInstance) {
		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
		beanDefinition.setLazyInit(false);
		beanDefinition.setAutowireCandidate(true);

		context.getBeanFactory().registerSingleton(beanName, beanInstance);
	}
}

