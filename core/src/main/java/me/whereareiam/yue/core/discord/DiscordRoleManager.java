package me.whereareiam.yue.core.discord;

import me.whereareiam.yue.api.event.ApplicationSuccessfullyStarted;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@DependsOn("discordBot")
public class DiscordRoleManager {
	private final Guild guild;

	@Autowired
	public DiscordRoleManager(@Lazy Guild guild) {
		this.guild = guild;
	}

	@EventListener(ApplicationSuccessfullyStarted.class)
	public void onEnable() {

	}
}
