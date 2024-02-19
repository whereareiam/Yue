package me.whereareiam.yue.core.command.commands;

import me.whereareiam.yue.core.command.base.CommandBase;
import me.whereareiam.yue.core.command.base.CommandCategory;
import me.whereareiam.yue.core.config.command.CommandsConfig;
import me.whereareiam.yue.core.config.command.LanguageCommandCommandsConfig;
import me.whereareiam.yue.core.util.message.MessageFormatterUtil;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LanguageCommand extends CommandBase {
	private final LanguageCommandCommandsConfig languageCommand;
	private final MessageFormatterUtil formatterUtil;

	@Autowired
	public LanguageCommand(CommandsConfig commandsConfig, MessageFormatterUtil formatterUtil) {
		super(commandsConfig);
		this.languageCommand = commandsConfig.getLanguageCommand();
		this.formatterUtil = formatterUtil;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		User user = event.getUser();

		event.getHook().sendMessage("Потом").queue();
	}

	@Override
	public List<String> getCommandAliases() {
		return languageCommand.getCommand();
	}

	@Override
	public List<? extends CommandData> getCommand() {
		List<SlashCommandData> commandData = getCommandAliases().stream()
				.map(command ->
						Commands.slash(command, formatterUtil.formatMessage(languageCommand.getDescription()))
				)
				.toList();

		commandData.forEach(command -> {
			command.setGuildOnly(isGuildOnly());
		});

		return commandData;
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.UTILITY;
	}

	@Override
	public String getRequiredRole() {
		return languageCommand.getRole();
	}

	@Override
	public List<String> getAllowedChannels() {
		return languageCommand.getAllowedChannels();
	}

	@Override
	public String getId() {
		return "languageCommand";
	}

	@Override
	public boolean isGuildOnly() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return languageCommand.isEnabled();
	}
}
