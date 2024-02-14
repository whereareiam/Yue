package me.whereareiam.yue.config.setting;

import me.whereareiam.yue.model.Language;

public class SettingsConfig {
	private Language defaultLanguage = Language.ENGLISH;
	private DatabaseSettingsConfig database = new DatabaseSettingsConfig();
	private DiscordSettingsConfig discord = new DiscordSettingsConfig();

	public Language getDefaultLanguage() {
		return defaultLanguage;
	}

	public DatabaseSettingsConfig getDatabase() {
		return database;
	}

	public DiscordSettingsConfig getDiscord() {
		return discord;
	}
}
