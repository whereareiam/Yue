package com.aeritt.yue.core.config;

import com.aeritt.yue.core.config.command.CommandsConfig;
import com.aeritt.yue.core.config.setting.SettingsConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Service
@DependsOn("beanRegistrationUtil")
public class ConfigInitializer {
	private final ConfigService configService;

	@Autowired
	public ConfigInitializer(ConfigService configService) {
		this.configService = configService;
	}

	@PostConstruct
	public void loadConfigs() {
		configService.registerConfig(SettingsConfig.class, "", "settings.json");
		configService.registerConfig(CommandsConfig.class, "", "commands.json");
	}
}