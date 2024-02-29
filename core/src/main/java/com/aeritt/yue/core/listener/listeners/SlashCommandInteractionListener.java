package com.aeritt.yue.core.listener.listeners;

import com.aeritt.yue.core.command.management.CommandService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SlashCommandInteractionListener extends ListenerAdapter {
	private final CommandService commandService;

	@Autowired
	public SlashCommandInteractionListener(CommandService commandService) {
		this.commandService = commandService;
	}

	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		if (!event.getUser().isBot())
			commandService.executeCommand(event);
	}
}
