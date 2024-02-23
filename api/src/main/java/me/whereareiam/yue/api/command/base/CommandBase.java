package me.whereareiam.yue.api.command.base;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public abstract class CommandBase {
	public abstract void execute(SlashCommandInteractionEvent event);

	public abstract List<String> getCommandAliases();

	public abstract List<? extends CommandData> getCommand();

	public abstract CommandCategory getCategory();

	public abstract String getRequiredRole();

	public abstract List<String> getAllowedChannels();

	public abstract String getId();

	public abstract boolean isGuildOnly();

	public abstract boolean isEnabled();
}
