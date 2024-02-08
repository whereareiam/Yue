package me.whereareiam.yue;

import me.whereareiam.yue.config.ConfigManager;
import me.whereareiam.yue.discord.DiscordBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class YueApplication {
	private static ApplicationContext applicationContext;
	private final DiscordBot discordBot;
	private final ConfigManager configManager;

	@Autowired
	public YueApplication(@Qualifier ApplicationContext applicationContext, ConfigManager configManager, DiscordBot discordBot) {
		YueApplication.applicationContext = applicationContext;
		this.configManager = configManager;
		this.discordBot = discordBot;
	}

	public static void main(String[] args) {
		SpringApplication.run(YueApplication.class, args);
		applicationContext.getBean(YueApplication.class).initialize();
	}

	public void initialize() {
		configManager.reload();
		discordBot.initializeDiscordBot();
	}
}
