package com.aeritt.yue.core.config.setting;

import lombok.Getter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Collection;
import java.util.List;

@Getter
public class DiscordSettingsConfig {
	private String guildId = "YOUR_GUILD_ID_HERE";
	private String guildOwner = "YOUR_GUILD_OWNER_ID_HERE";
	private String token = "YOUR_TOKEN_HERE";
	private boolean autoReconnect = true;
	private Collection<GatewayIntent> intents = List.of(
			GatewayIntent.GUILD_MEMBERS,
			GatewayIntent.GUILD_MESSAGES,
			GatewayIntent.GUILD_PRESENCES,
			GatewayIntent.MESSAGE_CONTENT
	);
}
