package me.whereareiam.yue.core.command.commands;

import me.whereareiam.yue.core.command.base.CommandBase;
import me.whereareiam.yue.core.command.base.CommandCategory;
import me.whereareiam.yue.core.config.command.CommandsConfig;
import me.whereareiam.yue.core.config.command.InfoCommandCommandsConfig;
import me.whereareiam.yue.core.service.PersonService;
import me.whereareiam.yue.core.util.message.MessageFormatterUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Lazy
@Component
public class InfoCommand extends CommandBase {
	private final InfoCommandCommandsConfig infoCommand;
	private final PersonService personService;
	private final Guild guild;

	@Autowired
	public InfoCommand(CommandsConfig commandsConfig, PersonService personService, Guild guild) {
		super(commandsConfig);
		this.infoCommand = commandsConfig.getInfoCommand();
		this.personService = personService;
		this.guild = guild;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		//event.deferReply(false).queue();
	}

	@Override
	public List<String> getCommandAliases() {
		return infoCommand.getCommand();
	}

	@Override
	public List<? extends CommandData> getCommand() {
		List<SlashCommandData> commandData = getCommandAliases().stream()
				.map(command ->
						Commands.slash(command, MessageFormatterUtil.formatMessage(infoCommand.getShortDescription()))
				)
				.toList();

		return commandData;
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.UTILITY;
	}

	@Override
	public String getRequiredRole() {
		return infoCommand.getRole();
	}

	@Override
	public List<String> getAllowedChannels() {
		return infoCommand.getAllowedChannels();
	}

	@Override
	public String getId() {
		return "infoCommand";
	}

	@Override
	public boolean isGuildOnly() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return infoCommand.isEnabled();
	}
}
