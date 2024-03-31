package com.aeritt.yue.api.config;

import com.aeritt.yue.api.config.database.DatabaseSettingsConfig;
import lombok.Getter;

@Getter
public class SettingsConfig {
	private String defaultLanguage = "us";
	private DatabaseSettingsConfig database = new DatabaseSettingsConfig();
}
