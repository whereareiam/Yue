package com.aeritt.yue.core.command.management;

import com.aeritt.yue.api.command.base.CommandBase;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CommandRegistrar implements com.aeritt.yue.api.command.management.CommandRegistrar {
	private final JDA jda;
	private final Guild guild;

	@Getter
	private final Set<CommandBase> commands = new HashSet<>();

	@Autowired
	public CommandRegistrar(@Lazy JDA jda, @Lazy Guild guild) {
		this.jda = jda;
		this.guild = guild;
	}

	public void registerCommand(CommandBase commandBase) {
		if (commands.stream().anyMatch(command -> command.getId().equals(commandBase.getId())))
			return;

		if (commandBase.isEnabled()) {
			commands.add(commandBase);
			if (commandBase.isGuildOnly()) {
				commandBase.getCommand().forEach(commandData -> guild.upsertCommand(commandData).queue());
			} else {
				commandBase.getCommand().forEach(commandData -> jda.upsertCommand(commandData).queue());
			}
		}
	}

	public void unregisterCommand(CommandBase commandBase) {
		if (commands.contains(commandBase)) {
			commands.remove(commandBase);

			if (commandBase.isGuildOnly()) {
				guild.deleteCommandById(commandBase.getId()).queue();
			} else {
				jda.deleteCommandById(commandBase.getId()).queue();
			}
		}
	}
}
