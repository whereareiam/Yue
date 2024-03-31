package com.aeritt.yue.config;

import com.aeritt.yue.config.command.CommandsConfig;
import com.aeritt.yue.config.setting.SettingsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigBean {
	@Autowired
	private ConfigService configService;

	@Bean
	public SettingsConfig settingsConfig() {
		return configService.registerConfig(SettingsConfig.class, "", "settings.json");
	}

	@Bean
	public CommandsConfig commandsConfig() {
		return configService.registerConfig(CommandsConfig.class, "", "commands.json");
	}
}