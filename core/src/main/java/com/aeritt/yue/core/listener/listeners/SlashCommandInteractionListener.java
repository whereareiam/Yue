package com.aeritt.yue.core.listener.listeners;

import com.aeritt.yue.api.discord.member.CacheReason;
import com.aeritt.yue.api.discord.member.DiscordMemberManager;
import com.aeritt.yue.core.command.management.CommandService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SlashCommandInteractionListener extends ListenerAdapter {
	private final DiscordMemberManager memberManager;
	private final CommandService commandService;

	@Autowired
	public SlashCommandInteractionListener(DiscordMemberManager memberManager, CommandService commandService) {
		this.memberManager = memberManager;
		this.commandService = commandService;
	}

	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		if (event.getUser().isBot()) return;

		memberManager.cacheMember(event.getUser().getId(), CacheReason.COMMAND);
		commandService.executeCommand(event);
	}
}
