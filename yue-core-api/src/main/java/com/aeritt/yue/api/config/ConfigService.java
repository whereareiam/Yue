package com.aeritt.yue.api.config;

public interface ConfigService {
	<T> T registerConfig(Class<T> configClass, String path, String fileName);

	<T> void unregisterConfig(Class<T> configClass);
}
