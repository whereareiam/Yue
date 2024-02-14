package me.whereareiam.yue.config.setting;

import lombok.Getter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Collection;
import java.util.List;

@Getter
public class DiscordSettingsConfig {
	private String guildId = "YOUR_GUILD_ID_HERE";
	private String token = "YOUR_TOKEN_HERE";
	private boolean autoReconnect = true;
	private Collection<GatewayIntent> intents = List.of(
			GatewayIntent.GUILD_MEMBERS,
			GatewayIntent.GUILD_MESSAGES,
			GatewayIntent.MESSAGE_CONTENT
	);
}
