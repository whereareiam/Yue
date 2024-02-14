package me.whereareiam.yue.discord.listener;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class GuildMemberJoinListener extends ListenerAdapter {
	@Override
	public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

	}
}
