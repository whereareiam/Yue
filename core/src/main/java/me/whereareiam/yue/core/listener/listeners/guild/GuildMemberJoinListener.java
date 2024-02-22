package me.whereareiam.yue.core.listener.listeners.guild;

import me.whereareiam.yue.core.config.setting.DiscordSettingsConfig;
import me.whereareiam.yue.core.config.setting.SettingsConfig;
import me.whereareiam.yue.core.feature.FeatureManager;
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