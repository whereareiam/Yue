package com.aeritt.yue.core.command.commands;

import com.aeritt.yue.api.command.base.CommandBase;
import com.aeritt.yue.api.command.base.CommandCategory;
import com.aeritt.yue.core.config.command.CommandsConfig;
import com.aeritt.yue.core.config.command.PluginCommandConfig;
import com.aeritt.yue.core.util.message.MessageFormatterUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PluginCommand extends CommandBase {
	private final MessageFormatterUtil messageFormatterUtil;
	private final PluginCommandConfig pluginCommand;

	@Autowired
	public PluginCommand(MessageFormatterUtil messageFormatterUtil, CommandsConfig commandsConfig) {
		this.messageFormatterUtil = messageFormatterUtil;
		this.pluginCommand = commandsConfig.getPluginCommand();
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		// Main embed shows all plugins (loaded, started, unstarted, unloaded)
		// Button control plugin: Shows another embed plugin and its id, adds row of buttons (1, 2, 3..)
		// (if more than 10 plugins, add pagination buttons to navigate through pages)
		// after click on button, shows another embed with plugin info, there we will be able
		// to start, stop, reload, unload plugin and delete.
		// Button back.
	}

	@Override
	public List<String> getCommandAliases() {
		return pluginCommand.getCommand();
	}

	@Override
	public List<? extends CommandData> getCommand() {
		List<SlashCommandData> commandData = getCommandAliases().stream()
				.map(command ->
						Commands.slash(command, messageFormatterUtil.formatMessage(pluginCommand.getShortDescription()))
				)
				.toList();

		return commandData;
	}

	@Override
	public CommandCategory getCategory() {
		return pluginCommand.getCategory();
	}

	@Override
	public String getRequiredRole() {
		return pluginCommand.getRoleId();
	}

	@Override
	public List<String> getAllowedChannels() {
		return pluginCommand.getAllowedChannels();
	}

	@Override
	public String getId() {
		return "pluginCommand";
	}

	@Override
	public boolean isGuildOnly() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return pluginCommand.isEnabled();
	}
}
