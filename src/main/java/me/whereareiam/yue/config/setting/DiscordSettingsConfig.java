package me.whereareiam.yue.config.setting;

import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Collection;
import java.util.List;

public class DiscordSettingsConfig {
	private String guildId = "YOUR_GUILD_ID_HERE";
	private String token = "YOUR_TOKEN_HERE";
	private boolean autoReconnect = true;
	private Collection<GatewayIntent> intents = List.of(
			GatewayIntent.GUILD_MEMBERS,
			GatewayIntent.GUILD_MESSAGES,
			GatewayIntent.MESSAGE_CONTENT
	);

	public String getGuildId() {
		return guildId;
	}

	public String getToken() {
		return token;
	}

	public boolean isAutoReconnect() {
		return autoReconnect;
	}

	public Collection<GatewayIntent> getIntents() {
		return intents;
	}
}
