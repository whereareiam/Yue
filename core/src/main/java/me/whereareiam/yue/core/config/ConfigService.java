package me.whereareiam.yue.core.config;

import jakarta.annotation.PostConstruct;
import me.whereareiam.yue.core.config.command.CommandsConfig;
import me.whereareiam.yue.core.config.component.ButtonsConfig;
import me.whereareiam.yue.core.config.component.EmbedsConfig;
import me.whereareiam.yue.core.config.component.palette.PaletteConfig;
import me.whereareiam.yue.core.config.feature.VerificationFeatureConfig;
import me.whereareiam.yue.core.config.setting.SettingsConfig;
import me.whereareiam.yue.core.util.BeanRegistrationUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class ConfigService implements ApplicationContextAware {
	private final BeanRegistrationUtil beanRegistrationUtil;
	private final Path dataFolder;
	private ConfigurableApplicationContext context;

	@Autowired
	public ConfigService(BeanRegistrationUtil beanRegistrationUtil, @Qualifier("dataPath") Path dataPath) {
		this.beanRegistrationUtil = beanRegistrationUtil;
		this.dataFolder = dataPath;
	}

	@PostConstruct
	public void loadConfigs() {
		registerConfigBean(SettingsConfig.class, "", "settings.json");
		registerConfigBean(RolesConfig.class, "", "roles.json");
		registerConfigBean(CommandsConfig.class, "", "commands.json");
		registerConfigBean(VerificationFeatureConfig.class, "feature", "verification.json");
		registerConfigBean(ButtonsConfig.class, "component", "buttons.json");
		registerConfigBean(PaletteConfig.class, "component", "palette.json");
		registerConfigBean(EmbedsConfig.class, "component", "embeds.json");
	}

	public void reloadConfigs() {
		reloadConfigBean(SettingsConfig.class, "", "settings.json");
		reloadConfigBean(RolesConfig.class, "", "roles.json");
		reloadConfigBean(CommandsConfig.class, "", "commands.json");
		reloadConfigBean(VerificationFeatureConfig.class, "feature", "verification.json");
		reloadConfigBean(ButtonsConfig.class, "component", "buttons.json");
		reloadConfigBean(PaletteConfig.class, "component", "palette.json");
		reloadConfigBean(EmbedsConfig.class, "component", "embeds.json");
	}

	private <T> void registerConfigBean(Class<T> configClass, String path, String fileName) {
		ConfigLoader<T> loader = new ConfigLoader<>(dataFolder);
		loader.load(configClass, path, fileName);
		T config = loader.getConfig();

		beanRegistrationUtil.registerSingleton(configClass.getName(), configClass, config);
	}

	private <T> void reloadConfigBean(Class<T> configClass, String path, String fileName) {
		ConfigLoader<T> loader = new ConfigLoader<>(dataFolder);
		loader.reload(configClass, path, fileName);
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