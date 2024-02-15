package me.whereareiam.yue.core.config.setting;

import lombok.Getter;

@Getter
public class SettingsConfig {
	private String defaultLanguage = "en";
	private DatabaseSettingsConfig database = new DatabaseSettingsConfig();
	private DiscordSettingsConfig discord = new DiscordSettingsConfig();
}
