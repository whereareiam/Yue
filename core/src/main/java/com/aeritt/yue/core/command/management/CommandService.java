package com.aeritt.yue.core.command.management;

import com.aeritt.yue.api.command.base.CommandBase;
import com.aeritt.yue.api.command.management.CommandRegistrar;
import com.aeritt.yue.api.discord.member.DiscordMemberManager;
import com.aeritt.yue.api.util.message.MessageSenderUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommandService {
	private final MessageSenderUtil messageSenderUtil;
	private final DiscordMemberManager memberManager;
	private final CommandRegistrar commandRegistrar;
	private final Guild guild;

	@Autowired
	public CommandService(MessageSenderUtil messageSenderUtil, DiscordMemberManager memberManager,
	                      CommandRegistrar commandRegistrar, @Lazy Guild guild) {
		this.messageSenderUtil = messageSenderUtil;
		this.memberManager = memberManager;
		this.commandRegistrar = commandRegistrar;
		this.guild = guild;
	}

	public void executeCommand(SlashCommandInteractionEvent event) {
		Optional<? extends CommandBase> optionalCommand = getCommand(event);

		if (optionalCommand.isEmpty()) return;

		CommandBase command = optionalCommand.get();
		if (!checkRequiredRole(command, event)) return;
		if (!checkAllowedChannels(command, event)) return;

		command.execute(event);
	}

	private Optional<? extends CommandBase> getCommand(SlashCommandInteractionEvent event) {
		return commandRegistrar.getCommands().stream()
				.filter(command -> command.getCommandAliases().contains(event.getName()))
				.findFirst();
	}

	private boolean checkAllowedChannels(CommandBase command, SlashCommandInteractionEvent event) {
		if (command.getAllowedChannels().isEmpty() || !event.isFromGuild()) return true;

		if (command.isGuildOnly() && !event.isFromGuild()) {
			messageSenderUtil.guildOnly(event.getHook());
			return false;
		}

		boolean isAllowed = command.getAllowedChannels().contains(event.getChannel().getId());

		if (!isAllowed) {
			event.deferReply(true).queue();
			messageSenderUtil.noRequiredChannel(
					event.getHook(),
					guild.getChannels().stream()
							.filter(channel -> command.getAllowedChannels().contains(channel.getId()))
							.map(Channel::getAsMention)
							.toList()
							.toArray(new String[0])
			);
		}
		return isAllowed;
	}

	private boolean checkRequiredRole(CommandBase command, SlashCommandInteractionEvent event) {
		if (command.getRequiredRole().isEmpty()) return true;
		Member member = memberManager.getMember(event.getUser().getId());

		boolean isAllowed = member.getRoles().stream()
				.anyMatch(role -> role.getId().equalsIgnoreCase(command.getRequiredRole()));

		if (!isAllowed) {
			event.deferReply(true).queue();
			messageSenderUtil.noRequiredRole(
					event.getHook(),
					guild.getRoleById(command.getRequiredRole()) == null ? command.getRequiredRole() : guild.getRoleById(command.getRequiredRole()).getAsMention()
			);
		}
		return isAllowed;
	}
}
