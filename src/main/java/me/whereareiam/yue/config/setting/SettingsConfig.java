package me.whereareiam.yue.config.setting;

import lombok.Getter;

@Getter
public class SettingsConfig {
	private String defaultLanguage = "en";
	private DatabaseSettingsConfig database = new DatabaseSettingsConfig();
	private DiscordSettingsConfig discord = new DiscordSettingsConfig();
}
