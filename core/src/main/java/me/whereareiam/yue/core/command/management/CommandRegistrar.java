package me.whereareiam.yue.core.command.management;

import lombok.Getter;
import me.whereareiam.yue.api.event.ApplicationSuccessfullyStarted;
import me.whereareiam.yue.core.command.base.CommandBase;
import me.whereareiam.yue.core.command.commands.HelpCommand;
import me.whereareiam.yue.core.command.commands.LanguageCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CommandRegistrar {
	private final ApplicationContext ctx;
	private final JDA jda;
	private final Guild guild;

	@Getter
	private final Set<CommandBase> commands = new HashSet<>();

	@Autowired
	public CommandRegistrar(@Qualifier ApplicationContext ctx, @Lazy JDA jda, @Lazy Guild guild) {
		this.ctx = ctx;
		this.jda = jda;
		this.guild = guild;
	}

	@EventListener(ApplicationSuccessfullyStarted.class)
	public void registerCommands() {
		jda.updateCommands().queue();

		registerCommand(ctx.getBean(HelpCommand.class));
		registerCommand(ctx.getBean(LanguageCommand.class));

		jda.updateCommands().addCommands(commands.stream().map(CommandBase::getCommand).flatMap(List::stream).toList()).queue();
	}

	public void registerCommand(CommandBase commandBase) {
		if (commands.stream().anyMatch(command -> command.getId().equals(commandBase.getId())))
			return;

		if (commandBase.isEnabled()) {
			commands.add(commandBase);
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
