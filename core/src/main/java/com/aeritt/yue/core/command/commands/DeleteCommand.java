package com.aeritt.yue.core.command.commands;

import com.aeritt.yue.api.command.base.CommandBase;
import com.aeritt.yue.api.command.base.CommandCategory;
import com.aeritt.yue.api.util.message.MessageBuilderUtil;
import com.aeritt.yue.api.util.message.MessageFormatterUtil;
import com.aeritt.yue.api.util.message.MessageSenderUtil;
import com.aeritt.yue.core.config.command.CommandsConfig;
import com.aeritt.yue.core.config.command.DeleteCommandConfig;
import com.aeritt.yue.core.service.PersonService;
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
	private final DeleteCommandConfig deleteCommand;
	private final MessageFormatterUtil messageFormatterUtil;
	private final MessageBuilderUtil messageBuilderUtil;
	private final MessageSenderUtil messageSenderUtil;
	private final PersonService personService;
	private final Guild guild;

	@Autowired
	public DeleteCommand(CommandsConfig commandsConfig, MessageFormatterUtil messageFormatterUtil,
	                     MessageBuilderUtil messageBuilderUtil, MessageSenderUtil messageSenderUtil,
	                     PersonService personService, @Lazy Guild guild) {
		this.deleteCommand = commandsConfig.getDeleteCommand();
		this.messageFormatterUtil = messageFormatterUtil;
		this.messageBuilderUtil = messageBuilderUtil;
		this.messageSenderUtil = messageSenderUtil;
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

			MessageEmbed embed = messageBuilderUtil.embed(
					deleteCommand.getEmbedId(),
					event.getUser(),
					Optional.empty()
			);

			event.getHook().sendMessageEmbeds(embed).queue();

			return;
		}

		messageSenderUtil.noRequiredUser(event.getHook(), userOption.getAsUser().getId());
	}

	@Override
	public List<String> getCommandAliases() {
		return deleteCommand.getCommand();
	}

	@Override
	public List<? extends CommandData> getCommand() {
		List<SlashCommandData> commandData = getCommandAliases().stream()
				.map(command ->
						Commands.slash(command, messageFormatterUtil.formatMessage(deleteCommand.getShortDescription()))
				)
				.toList();

		commandData.forEach(command -> {
			command.addOption(
					OptionType.USER,
					"user",
					messageFormatterUtil.formatMessage(
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
		return deleteCommand.getRoleId();
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
