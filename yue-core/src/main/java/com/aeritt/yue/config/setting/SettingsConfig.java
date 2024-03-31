package com.aeritt.yue.config.setting;

import lombok.Getter;

@Getter
public class SettingsConfig extends com.aeritt.yue.api.config.SettingsConfig {
	private DiscordSettingsConfig discord = new DiscordSettingsConfig();
}