package com.aeritt.yue.core.listener.listeners.guild;

import com.aeritt.yue.core.config.configs.setting.DiscordSettingsConfig;
import com.aeritt.yue.core.config.configs.setting.SettingsConfig;
import com.aeritt.yue.core.feature.FeatureManager;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuildMemberJoinListener extends ListenerAdapter {
	private final DiscordSettingsConfig discordSettingsConfig;
	private final FeatureManager featureManager;

	@Autowired
	public GuildMemberJoinListener(SettingsConfig settingsConfig, FeatureManager featureManager) {
		this.featureManager = featureManager;
		this.discordSettingsConfig = settingsConfig.getDiscord();
	}

	@Override
	public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
		if (discordSettingsConfig.getGuildId().equals(event.getGuild().getId())) {
			featureManager.notifyFeatures(event);
		}
	}
}