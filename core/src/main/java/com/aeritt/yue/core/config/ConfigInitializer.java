package com.aeritt.yue.core.config;

import jakarta.annotation.PostConstruct;
import com.aeritt.yue.core.config.configs.command.CommandsConfig;
import com.aeritt.yue.core.config.configs.feature.VerificationFeatureConfig;
import com.aeritt.yue.core.config.configs.setting.SettingsConfig;
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
		configService.registerConfig(RolesConfig.class, "", "roles.json");
		configService.registerConfig(CommandsConfig.class, "", "commands.json");

		configService.registerConfig(VerificationFeatureConfig.class, "feature", "verification.json");
	}
}