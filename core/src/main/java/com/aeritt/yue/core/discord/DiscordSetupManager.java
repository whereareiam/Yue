package com.aeritt.yue.core.discord;

import com.aeritt.yue.api.event.ApplicationBotStarted;
import com.aeritt.yue.api.util.BeanRegistrationUtil;
import com.aeritt.yue.core.config.setting.DiscordSettingsConfig;
import com.aeritt.yue.core.config.setting.SettingsConfig;
import com.aeritt.yue.core.language.LanguageService;
import jakarta.annotation.PreDestroy;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@DependsOn("databaseSetupManager")
public class DiscordSetupManager {
	private final BeanRegistrationUtil beanRegistrationUtil;
	private final LanguageService languageService;
	private final SettingsConfig settingsConfig;
	private JDA jda;

	@Autowired
	public DiscordSetupManager(BeanRegistrationUtil beanRegistrationUtil, LanguageService languageService,
	                           SettingsConfig settingsConfig) {
		this.beanRegistrationUtil = beanRegistrationUtil;
		this.languageService = languageService;
		this.settingsConfig = settingsConfig;
	}

	@Order(Ordered.HIGHEST_PRECEDENCE)
	@EventListener(ApplicationReadyEvent.class)
	public void onLoad() {
		try {
			DiscordSettingsConfig discordSettingsConfig = settingsConfig.getDiscord();
			JDABuilder bot = JDABuilder
					.createDefault(discordSettingsConfig.getToken())
					.setChunkingFilter(ChunkingFilter.ALL)
					.enableIntents(discordSettingsConfig.getIntents())
					.setMemberCachePolicy(MemberCachePolicy.ONLINE)
					.setAutoReconnect(discordSettingsConfig.isAutoReconnect());

			jda = bot.build();
			beanRegistrationUtil.registerSingleton("jda", JDA.class, jda);
		} catch (InvalidTokenException e) {
			throw new RuntimeException("Discord token is invalid. Please check your settings.json file.");
		}

		jda.getPresence().setActivity(Activity.playing(
				languageService.getTranslation("core.main.phase.loading")
		));
	}

	@Order(Ordered.HIGHEST_PRECEDENCE)
	@EventListener(ApplicationBotStarted.class)
	public void onEnable() {
		beanRegistrationUtil.registerSingleton("guild", Guild.class, jda.getGuildById(settingsConfig.getDiscord().getGuildId()));
		jda.getGuilds().stream()
				.filter(guild -> !guild.getId().equals(settingsConfig.getDiscord().getGuildId()))
				.forEach(Guild::leave);

		jda.getPresence().setActivity(Activity.playing(
				languageService.getTranslation("core.main.phase.starting")
		));
	}

	@PreDestroy
	public void onDisable() {
		jda.shutdown();
	}
}
