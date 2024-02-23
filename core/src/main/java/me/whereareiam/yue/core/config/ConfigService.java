package me.whereareiam.yue.core.config;

import me.whereareiam.yue.core.util.BeanRegistrationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Service
public class ConfigService implements me.whereareiam.yue.api.config.ConfigService {
	private final Path dataPath;
	private final Set<Class<?>> registeredConfigs = new HashSet<>();

	@Autowired
	public ConfigService(@Qualifier("dataPath") Path dataPath) {
		this.dataPath = dataPath;
	}

	@Override
	public <T> boolean registerConfig(Class<T> configClass, String path, String fileName) {
		if (registeredConfigs.contains(configClass) || path == null || fileName == null) return false;

		ConfigLoader<T> configLoader = new ConfigLoader<>(dataPath);
		configLoader.load(configClass, path, fileName);
		T object = configLoader.getConfig();

		BeanRegistrationUtil.registerSingleton(configClass.getName(), configClass, object);
		registeredConfigs.add(configClass);

		return true;
	}

	@Override
	public <T> void unregisterConfig(Class<T> configClass) {
		if (registeredConfigs.contains(configClass)) {
			BeanRegistrationUtil.destroyBean(configClass.getName());
			registeredConfigs.remove(configClass);
		}
	}
}