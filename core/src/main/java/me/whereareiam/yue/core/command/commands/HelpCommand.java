package me.whereareiam.yue.core.command.commands;

import me.whereareiam.yue.core.command.base.CommandBase;
import me.whereareiam.yue.core.command.base.CommandCategory;
import me.whereareiam.yue.core.config.command.CommandsConfig;
import me.whereareiam.yue.core.config.command.HelpCommandCommandsConfig;
import me.whereareiam.yue.core.util.message.MessageFormatterUtil;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HelpCommand extends CommandBase {
	private final HelpCommandCommandsConfig helpCommand;
	private final MessageFormatterUtil formatterUtil;

	@Autowired
	public HelpCommand(CommandsConfig commandsConfig, MessageFormatterUtil formatterUtil) {
		super(commandsConfig);
		this.helpCommand = commandsConfig.getHelpCommand();
		this.formatterUtil = formatterUtil;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		User user = event.getUser();
		if (event.getOption("command") != null) {
			event.getHook().sendMessage("Ты опциональный пидор").queue();
			return;
		}

		event.getHook().sendMessage("Ты пидор").queue();
	}

	@Override
	public List<String> getCommandAliases() {
		return helpCommand.getCommand();
	}

	@Override
	public List<? extends CommandData> getCommand() {
		List<SlashCommandData> commandData = getCommandAliases().stream()
				.map(command ->
						Commands.slash(command, formatterUtil.formatMessage(helpCommand.getDescription()))
				)
				.toList();

		commandData.forEach(command -> {
			command.addOption(
					OptionType.STRING,
					"command",
					formatterUtil.formatMessage(helpCommand.getCommandOption()),
					false
			);

			command.setGuildOnly(isGuildOnly());
		});

		return commandData;
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.GENERAL;
	}

	@Override
	public String getRequiredRole() {
		return helpCommand.getRole();
	}

	@Override
	public List<String> getAllowedChannels() {
		return helpCommand.getAllowedChannels();
	}

	@Override
	public String getId() {
		return "helpCommand";
	}

	@Override
	public boolean isGuildOnly() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return helpCommand.isEnabled();
	}
}
