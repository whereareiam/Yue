package me.whereareiam.yue.core.discord;

import jakarta.annotation.PreDestroy;
import me.whereareiam.yue.api.event.ApplicationSuccessfullyStarted;
import me.whereareiam.yue.core.Scheduler;
import me.whereareiam.yue.core.config.setting.DiscordSettingsConfig;
import me.whereareiam.yue.core.config.setting.SettingsConfig;
import me.whereareiam.yue.core.language.LanguageManager;
import me.whereareiam.yue.core.listener.pubisher.ApplicationSuccessfullyStartedPublisher;
import me.whereareiam.yue.core.util.BeanRegistrationUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Component
@DependsOn("databaseManager")
public class DiscordBot {
	private final BeanRegistrationUtil beanRegistrationUtil;
	private final DiscordListenerRegistrar discordListenerRegistrar;
	private final ApplicationSuccessfullyStartedPublisher eventPublisher;
	private final LanguageManager languageManager;
	private final Logger logger;
	private final Scheduler scheduler;
	private final SettingsConfig settingsConfig;
	private JDA jda;

	@Autowired
	public DiscordBot(BeanRegistrationUtil beanRegistrationUtil, @Lazy DiscordListenerRegistrar discordListenerRegistrar,
	                  ApplicationSuccessfullyStartedPublisher eventPublisher, LanguageManager languageManager, Logger logger,
	                  Scheduler scheduler, SettingsConfig settingsConfig) {
		this.beanRegistrationUtil = beanRegistrationUtil;
		this.discordListenerRegistrar = discordListenerRegistrar;
		this.eventPublisher = eventPublisher;
		this.languageManager = languageManager;
		this.logger = logger;
		this.scheduler = scheduler;
		this.settingsConfig = settingsConfig;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onLoad() {
		try {
			DiscordSettingsConfig discordSettingsConfig = settingsConfig.getDiscord();
			JDABuilder bot = JDABuilder
					.createDefault(discordSettingsConfig.getToken())
					.enableIntents(discordSettingsConfig.getIntents())
					.setMemberCachePolicy(MemberCachePolicy.ONLINE)
					.setAutoReconnect(discordSettingsConfig.isAutoReconnect());

			jda = bot.build();
			beanRegistrationUtil.registerSingleton("jda", JDA.class, jda);
		} catch (InvalidTokenException e) {
			logger.severe("Discord token is invalid. Please check your settings.json file.");
			System.exit(1);
		}

		jda.getPresence().setActivity(Activity.playing(
				languageManager.getTranslation("core.main.phase.loading")
		));

		jda.updateCommands().queue();
		discordListenerRegistrar.registerListeners();
		scheduler.schedule(eventPublisher::publish, 1, TimeUnit.SECONDS);
	}

	@EventListener(ApplicationSuccessfullyStarted.class)
	public void onEnable() {
		beanRegistrationUtil.registerSingleton("guild", Guild.class, jda.getGuildById(settingsConfig.getDiscord().getGuildId()));
		jda.getPresence().setActivity(Activity.playing(
				languageManager.getTranslation("core.main.phase.starting")
		));
	}

	@PreDestroy
	public void onDisable() {
		jda.shutdown();
	}
}
