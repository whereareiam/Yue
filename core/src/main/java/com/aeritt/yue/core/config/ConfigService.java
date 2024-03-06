package com.aeritt.yue.core.config;

import com.aeritt.yue.api.util.BeanRegistrationUtil;
import com.aeritt.yue.core.SpringPluginManager;
import org.pf4j.PluginWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Service
public class ConfigService implements com.aeritt.yue.api.config.ConfigService {
	private final Path dataPath;
	private final SpringPluginManager pluginManager;
	private final BeanRegistrationUtil beanRegistrationUtil;
	private final Set<Class<?>> registeredConfigs = new HashSet<>();

	@Autowired
	public ConfigService(@Qualifier("dataPath") Path dataPath, SpringPluginManager pluginManager,
	                     BeanRegistrationUtil beanRegistrationUtil) {
		this.dataPath = dataPath;
		this.pluginManager = pluginManager;
		this.beanRegistrationUtil = beanRegistrationUtil;
	}

	@Override
	public <T> boolean registerConfig(Class<T> configClass, String path, String fileName) {
		if (registeredConfigs.contains(configClass) || path == null || fileName == null) return false;

		String packageName = configClass.getPackage().getName();
		Path pathToUse = dataPath;

		if (!packageName.startsWith("com.aeritt.core.yue.")) {
			for (PluginWrapper plugin : pluginManager.getPlugins()) {
				Package[] packages = plugin.getPluginClassLoader().getDefinedPackages();
				for (Package pkg : packages) {
					if (pkg.getName().equals(packageName)) {
						pathToUse = Paths.get(dataPath.toString(), "plugins", plugin.getPluginId());
						break;
					}
				}
			}
		}

		ConfigLoader<T> configLoader = new ConfigLoader<>(pathToUse);
		configLoader.load(configClass, path, fileName);
		T object = configLoader.getConfig();

		beanRegistrationUtil.registerSingleton(configClass.getName(), configClass, object);
		registeredConfigs.add(configClass);

		return true;
	}

	@Override
	public <T> void unregisterConfig(Class<T> configClass) {
		if (registeredConfigs.contains(configClass)) {
			beanRegistrationUtil.destroyBean(configClass.getName());
			registeredConfigs.remove(configClass);
		}
	}
}