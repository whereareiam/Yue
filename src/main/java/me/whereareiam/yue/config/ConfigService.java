package me.whereareiam.yue.config;

import jakarta.annotation.PostConstruct;
import me.whereareiam.yue.config.setting.SettingsConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class ConfigService implements ApplicationContextAware {
	private final Path dataFolder;
	private ConfigurableApplicationContext context;

	@Autowired
	public ConfigService(@Qualifier("dataPath") Path dataPath) {
		this.dataFolder = dataPath;
	}

	@PostConstruct
	public void loadConfigs() {
		registerConfigBean(SettingsConfig.class, "settings.json");
	}

	public void reloadConfigs() {
		reloadConfigBean(SettingsConfig.class, "settings.json");
	}

	private <T> void registerConfigBean(Class<T> configClass, String fileName) {
		ConfigLoader<T> loader = new ConfigLoader<>(dataFolder);
		loader.load(configClass, fileName);
		T config = loader.getConfig();

		BeanDefinitionRegistry registry = (BeanDefinitionRegistry) context.getBeanFactory();
		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClass(configClass);
		beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
		beanDefinition.setLazyInit(false);
		beanDefinition.setAutowireCandidate(true);
		registry.registerBeanDefinition(fileName, beanDefinition);
		context.getBeanFactory().registerSingleton(fileName, config);
	}

	private <T> void reloadConfigBean(Class<T> configClass, String fileName) {
		ConfigLoader<T> loader = new ConfigLoader<>(dataFolder);
		loader.reload(configClass, fileName);
		T config = loader.getConfig();

		DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) context.getBeanFactory();
		registry.destroySingleton(fileName);
		context.getBeanFactory().registerSingleton(fileName, config);
	}

	@Override
	public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
		this.context = (ConfigurableApplicationContext) applicationContext;
	}
}