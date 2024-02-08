package me.whereareiam.yue.config;

import me.whereareiam.yue.config.setting.SettingsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class ConfigManager {
	private final Path dataFolder;
	private final SettingsConfig settingsConfig;

	@Autowired
	public ConfigManager(@Qualifier("dataPath") Path dataPath, SettingsConfig settingsConfig) {
		this.dataFolder = dataPath;
		this.settingsConfig = settingsConfig;
	}

	public void load() {
		settingsConfig.load(dataFolder.resolve("settings.yml"));
	}

	public void save() {
		settingsConfig.save(dataFolder.resolve("settings.yml"));
	}

	public void reload() {
		settingsConfig.reload(dataFolder.resolve("settings.yml"));
	}
}
