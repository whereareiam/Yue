package com.aeritt.yue.listener;

import com.aeritt.yue.api.service.member.CacheReason;
import com.aeritt.yue.api.service.member.DiscordMemberService;
import com.aeritt.yue.command.management.CommandService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SlashCommandInteractionListener extends ListenerAdapter {
	private final DiscordMemberService memberManager;
	private final CommandService commandService;

	@Autowired
	public SlashCommandInteractionListener(DiscordMemberService memberManager, CommandService commandService) {
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
