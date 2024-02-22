package me.whereareiam.yue.core.command.commands;

import me.whereareiam.yue.core.command.base.CommandBase;
import me.whereareiam.yue.core.command.base.CommandCategory;
import me.whereareiam.yue.core.config.command.CommandsConfig;
import me.whereareiam.yue.core.config.command.DeleteCommandCommandsConfig;
import me.whereareiam.yue.core.service.PersonService;
import me.whereareiam.yue.core.util.message.MessageBuilderUtil;
import me.whereareiam.yue.core.util.message.MessageFormatterUtil;
import me.whereareiam.yue.core.util.message.MessageSenderUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Lazy
@Component
public class DeleteCommand extends CommandBase {
	private final DeleteCommandCommandsConfig deleteCommand;
	private final PersonService personService;
	private final Guild guild;

	@Autowired
	public DeleteCommand(CommandsConfig commandsConfig, PersonService personService, Guild guild) {
		super(commandsConfig);
		this.deleteCommand = commandsConfig.getDeleteCommand();
		this.personService = personService;
		this.guild = guild;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		OptionMapping userOption = event.getOption("user");
		if (userOption != null && userOption.getAsMember() != null && guild.getSelfMember().canInteract(userOption.getAsMember())) {
			personService.deleteUser(userOption.getAsMember().getId());
			guild.kick(userOption.getAsMember()).queue();

			MessageEmbed embed = MessageBuilderUtil.embed(
					"deleteCommandSuccess",
					event.getUser(),
					Optional.empty()
			);

			event.getHook().sendMessageEmbeds(embed).queue();

			return;
		}

		MessageSenderUtil.noRequiredUser(event.getHook(), userOption.getAsUser().getId());
	}

	@Override
	public List<String> getCommandAliases() {
		return deleteCommand.getCommand();
	}

	@Override
	public List<? extends CommandData> getCommand() {
		List<SlashCommandData> commandData = getCommandAliases().stream()
				.map(command ->
						Commands.slash(command, MessageFormatterUtil.formatMessage(deleteCommand.getShortDescription()))
				)
				.toList();

		commandData.forEach(command -> {
			command.addOption(
					OptionType.USER,
					"user",
					MessageFormatterUtil.formatMessage(
							deleteCommand.getShortDescription().replace("shortDescription", "options.user.shortDescription")
					),
					true
			);
		});

		return commandData;
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.STAFF;
	}

	@Override
	public String getRequiredRole() {
		return deleteCommand.getRole();
	}

	@Override
	public List<String> getAllowedChannels() {
		return deleteCommand.getAllowedChannels();
	}

	@Override
	public String getId() {
		return "deleteCommand";
	}

	@Override
	public boolean isGuildOnly() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return deleteCommand.isEnabled();
	}
}
