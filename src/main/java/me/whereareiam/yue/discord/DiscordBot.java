package me.whereareiam.yue.discord;

import me.whereareiam.yue.config.setting.DiscordSettingsConfig;
import me.whereareiam.yue.config.setting.SettingsConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiscordBot {
	private final SettingsConfig settingsConfig;
	private JDA jda = null;

	@Autowired
	public DiscordBot(SettingsConfig settingsConfig) {
		this.settingsConfig = settingsConfig;
	}

	public void initializeDiscordBot() {
		DiscordSettingsConfig discordSettingsConfig = settingsConfig.discord;

		JDABuilder jdaBuilder = JDABuilder.createDefault(discordSettingsConfig.token);

		jdaBuilder.setActivity(Activity.playing("starting up..."));
		jdaBuilder.setAutoReconnect(discordSettingsConfig.autoReconnect);

		setJda(jdaBuilder.build());
	}

	public JDA getJda() {
		return jda;
	}

	public void setJda(JDA jda) {
		this.jda = jda;
	}
}
