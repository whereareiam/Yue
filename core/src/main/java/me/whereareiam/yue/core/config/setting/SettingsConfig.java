package me.whereareiam.yue.core.config.setting;

import lombok.Getter;
import me.whereareiam.yue.core.config.setting.database.DatabaseSettingsConfig;

@Getter
public class SettingsConfig {
	private String defaultLanguage = "us";
	private DatabaseSettingsConfig database = new DatabaseSettingsConfig();
	private DiscordSettingsConfig discord = new DiscordSettingsConfig();
	private FeaturesSettingsConfig features = new FeaturesSettingsConfig();
}
