package me.whereareiam.yue.core.command.commands;

import me.whereareiam.yue.api.command.base.CommandBase;
import me.whereareiam.yue.api.command.base.CommandCategory;
import me.whereareiam.yue.core.database.entity.Language;
import me.whereareiam.yue.core.config.command.CommandsConfig;
import me.whereareiam.yue.core.config.command.InfoCommandCommandsConfig;
import me.whereareiam.yue.core.database.repository.LanguageRepository;
import me.whereareiam.yue.core.database.repository.PersonRepository;
import me.whereareiam.yue.core.util.message.MessageBuilderUtil;
import me.whereareiam.yue.core.util.message.MessageFormatterUtil;
import me.whereareiam.yue.core.util.message.PlaceholderReplacement;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Lazy
@Component
public class InfoCommand extends CommandBase {
	private final InfoCommandCommandsConfig infoCommand;
	private final LanguageRepository languageRepository;
	private final PersonRepository personRepository;

	private final OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
	private final RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
	private final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

	@Autowired
	public InfoCommand(CommandsConfig commandsConfig, LanguageRepository languageRepository,
	                   PersonRepository personRepository) {
		this.infoCommand = commandsConfig.getInfoCommand();
		this.languageRepository = languageRepository;
		this.personRepository = personRepository;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		User user = event.getUser();

		PlaceholderReplacement replacement = createPlaceholderReplacement();

		MessageEmbed embed = MessageBuilderUtil.embed("info", user, Optional.of(replacement));

		event.getHook().sendMessageEmbeds(embed).queue();
	}

	private PlaceholderReplacement createPlaceholderReplacement() {
		DecimalFormat df = new DecimalFormat("00.0");

		String cpuLoad = df.format(osBean.getSystemLoadAverage());
		String cpuCores = String.valueOf(osBean.getAvailableProcessors());
		String memoryUsage = String.valueOf(getUsedMemory());
		String memoryTotal = String.valueOf(getTotalMemory());
		String memoryUsagePercentage = df.format(((double) getUsedMemory() / (double) getTotalMemory()) * 100);
		String uptime = String.format("%.1f", runtimeBean.getUptime() / 1000.0 / 60.0 / 60.0);
		String usersCount = String.valueOf(personRepository.count());
		final String languages = languageRepository.findAll().stream()
				.map(Language::getCode)
				.collect(Collectors.joining(", "));

		return new PlaceholderReplacement(
				List.of("{osName}", "{osVersion}", "{osArch}", "{cpuLoad}", "{cpuCores}",
						"{memoryUsage}", "{memoryTotal}", "{memoryUsagePercentage}", "{uptime}",
						"{usersCount}", "{languages}"),
				List.of(System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"),
						cpuLoad, cpuCores, memoryUsage, memoryTotal, memoryUsagePercentage, uptime, usersCount, languages)
		);
	}

	private long getUsedMemory() {
		return memoryBean.getHeapMemoryUsage().getUsed() / 1024 / 1024;
	}

	private long getTotalMemory() {
		return memoryBean.getHeapMemoryUsage().getMax() / 1024 / 1024;
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
