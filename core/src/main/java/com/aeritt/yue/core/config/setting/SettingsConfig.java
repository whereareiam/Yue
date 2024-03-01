package com.aeritt.yue.core.config.setting;

import com.aeritt.yue.core.config.setting.database.DatabaseSettingsConfig;
import lombok.Getter;

@Getter
public class SettingsConfig {
	private String defaultLanguage = "us";
	private DatabaseSettingsConfig database = new DatabaseSettingsConfig();
	private DiscordSettingsConfig discord = new DiscordSettingsConfig();
}
