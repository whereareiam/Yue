package com.aeritt.yue.core.config.configs.setting;

import lombok.Getter;
import com.aeritt.yue.core.config.configs.setting.database.DatabaseSettingsConfig;

@Getter
public class SettingsConfig {
	private String defaultLanguage = "us";
	private DatabaseSettingsConfig database = new DatabaseSettingsConfig();
	private DiscordSettingsConfig discord = new DiscordSettingsConfig();
	private FeaturesSettingsConfig features = new FeaturesSettingsConfig();
}
