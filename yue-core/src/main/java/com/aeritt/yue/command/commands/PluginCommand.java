package com.aeritt.yue.command.commands;

import com.aeritt.yue.SpringPluginManager;
import com.aeritt.yue.api.command.base.CommandBase;
import com.aeritt.yue.api.command.base.CommandCategory;
import com.aeritt.yue.api.message.PlaceholderReplacement;
import com.aeritt.yue.config.command.CommandsConfig;
import com.aeritt.yue.config.command.PluginCommandConfig;
import com.aeritt.yue.service.DiscordButtonService;
import com.aeritt.yue.util.message.MessageBuilder;
import com.aeritt.yue.util.message.MessageFormatter;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.pf4j.PluginState;
import org.pf4j.PluginWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PluginCommand extends CommandBase {
	private final MessageBuilder messageBuilder;
	private final MessageFormatter messageFormatter;
	private final PluginCommandConfig pluginCommand;
	private final SpringPluginManager pluginManager;
	private final DiscordButtonService buttonManager;

	@Autowired
	public PluginCommand(MessageBuilder messageBuilder, MessageFormatter messageFormatter, CommandsConfig commandsConfig,
	                     SpringPluginManager pluginManager, DiscordButtonService buttonManager) {
		this.messageBuilder = messageBuilder;
		this.messageFormatter = messageFormatter;
		this.pluginCommand = commandsConfig.getPluginCommand();
		this.pluginManager = pluginManager;
		this.buttonManager = buttonManager;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();

		String pluginFormat = pluginCommand.getPluginFormat();

		int loadedPluginsCount = pluginManager.getPlugins(PluginState.RESOLVED).size();
		int enabledPluginsCount = pluginManager.getPlugins(PluginState.STARTED).size();
		int disabledPluginsCount = pluginManager.getPlugins(PluginState.DISABLED).size();
		PlaceholderReplacement replacements = new PlaceholderReplacement(
				List.of("{pluginCount}", "{loadedPluginsCount}", "{enabledPluginsCount}", "{disabledPluginsCount}",
						"{loadedPlugins}", "{enabledPlugins}", "{disabledPlugins}"),
				List.of(
						String.valueOf(pluginManager.getPlugins().size()),
						String.valueOf(loadedPluginsCount),
						String.valueOf(enabledPluginsCount),
						String.valueOf(disabledPluginsCount),
						formatPlugins(pluginManager.getPlugins(PluginState.RESOLVED), pluginFormat, event.getUser()),
						formatPlugins(pluginManager.getPlugins(PluginState.STARTED), pluginFormat, event.getUser()),
						formatPlugins(pluginManager.getPlugins(PluginState.DISABLED), pluginFormat, event.getUser())
				)
		);

		MessageEmbed embed = messageBuilder.embed(
				pluginCommand.getEmbedId(),
				event.getUser(),
				Optional.of(replacements)
		);

		List<Button> buttons = new ArrayList<>();
		if (loadedPluginsCount > 0) {
			buttons.add(messageBuilder.button("core.button.commands.loadedPlugins", event.getUser()));
			buttonManager.addButton("core.button.commands.loadedPlugins", this::handleButtonInteraction);
		}
		if (enabledPluginsCount > 0) {
			buttons.add(messageBuilder.button("core.button.commands.enabledPlugins", event.getUser()));
			buttonManager.addButton("core.button.commands.enabledPlugins", this::handleButtonInteraction);
		}
		if (disabledPluginsCount > 0) {
			buttons.add(messageBuilder.button("core.button.commands.disabledPlugins", event.getUser()));
			buttonManager.addButton("core.button.commands.disabledPlugins", this::handleButtonInteraction);
		}

		if (buttons.isEmpty())
			buttons.add(messageBuilder.button("core.button.commands.noPlugins", event.getUser()));

		event.getHook().sendMessageEmbeds(embed).setActionRow(buttons).queue();
	}

	private void handleButtonInteraction(ButtonInteractionEvent event) {

	}

	private String formatPlugins(List<PluginWrapper> plugins, String pluginFormat, User user) {
		return plugins.stream()
				.map(plugin -> pluginFormat
						.replace("{pluginName}", plugin.getDescriptor().getPluginId())
						.replace("{pluginVersion}", plugin.getDescriptor().getVersion())
				)
				.reduce((plugin1, plugin2) -> plugin1 + "\n" + plugin2)
				.orElse(messageFormatter.formatMessage(user, "core.dictionary.absent"));
	}

	@Override
	public List<String> getCommandAliases() {
		return pluginCommand.getCommand();
	}

	@Override
	public List<? extends CommandData> getCommand() {
		List<SlashCommandData> commandData = getCommandAliases().stream()
				.map(command ->
						Commands.slash(command, messageFormatter.formatMessage(pluginCommand.getShortDescription()))
				)
				.toList();

		commandData.forEach(command -> command.setGuildOnly(isGuildOnly()));

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