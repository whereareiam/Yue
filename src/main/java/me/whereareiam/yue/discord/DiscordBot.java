package me.whereareiam.yue.discord;

import jakarta.annotation.PreDestroy;
import me.whereareiam.yue.config.setting.DiscordSettingsConfig;
import me.whereareiam.yue.config.setting.SettingsConfig;
import me.whereareiam.yue.language.LanguageManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class DiscordBot {
	private final ConfigurableApplicationContext context;
	private final Logger logger;
	private final SettingsConfig settingsConfig;
	private final DiscordListenerRegistrar discordListenerRegistrar;
	private final LanguageManager languageManager;
	private JDA jda;

	@Autowired
	public DiscordBot(ConfigurableApplicationContext context, Logger logger, SettingsConfig settingsConfig,
	                  @Lazy DiscordListenerRegistrar discordListenerRegistrar, LanguageManager languageManager) {
		this.logger = logger;
		this.context = context;
		this.settingsConfig = settingsConfig;
		this.discordListenerRegistrar = discordListenerRegistrar;
		this.languageManager = languageManager;
	}

	@EventListener(ApplicationStartedEvent.class)
	public void onLoad() {
		try {
			DiscordSettingsConfig discordSettingsConfig = settingsConfig.getDiscord();
			JDABuilder bot = JDABuilder
					.createDefault(discordSettingsConfig.getToken())
					.enableIntents(discordSettingsConfig.getIntents())
					.setMemberCachePolicy(MemberCachePolicy.ONLINE)
					.setAutoReconnect(discordSettingsConfig.isAutoReconnect());

			jda = bot.build();
			registerJDA();
		} catch (InvalidTokenException e) {
			logger.severe("Discord token is invalid. Please check your settings.json file.");
			System.exit(1);
		}

		jda.getPresence().setActivity(Activity.playing(
				languageManager.getTranslation("core.main.phase.loading")
		));

		jda.updateCommands().queue();

		discordListenerRegistrar.registerListeners();
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onEnable() {
		jda.getPresence().setActivity(Activity.playing(
				languageManager.getTranslation("core.main.phase.starting")
		));
	}

	@PreDestroy
	public void onDisable() {
		jda.shutdown();
	}

	private void registerJDA() {
		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClass(JDA.class);
		beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
		beanDefinition.setLazyInit(false);
		beanDefinition.setAutowireCandidate(true);
		context.getBeanFactory().registerSingleton("JDA", jda);
	}
}
