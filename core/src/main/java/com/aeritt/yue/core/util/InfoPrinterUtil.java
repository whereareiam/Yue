package com.aeritt.yue.core.util;

import com.aeritt.yue.api.command.management.CommandRegistrar;
import com.aeritt.yue.api.language.LanguageService;
import com.aeritt.yue.core.SpringPluginManager;
import com.aeritt.yue.core.component.ComponentService;
import com.aeritt.yue.core.database.repository.person.PersonRepository;
import com.aeritt.yue.core.event.ApplicationBotStarted;
import net.dv8tion.jda.api.JDA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Lazy
@Service
@DependsOn({"discordSetupManager", "commandRegistrar"})
public class InfoPrinterUtil {
	private final PersonRepository personRepository;
	private final CommandRegistrar commandRegistrar;
	private final ComponentService componentService;
	private final SpringPluginManager pluginManager;
	private final LanguageService languageService;
	private final String version;
	private final Logger logger;
	private final JDA jda;

	@Autowired
	public InfoPrinterUtil(PersonRepository personRepository, CommandRegistrar commandRegistrar, ComponentService componentService,
	                       SpringPluginManager pluginManager, LanguageService languageService, @Qualifier("version") String version, Logger logger,
	                       @Lazy JDA jda) {
		this.commandRegistrar = commandRegistrar;
		this.componentService = componentService;
		this.pluginManager = pluginManager;
		this.languageService = languageService;
		this.version = version;
		this.logger = logger;
		this.personRepository = personRepository;
		this.jda = jda;
	}

	@Async
	@EventListener(ApplicationBotStarted.class)
	public void printStartMessage() {
		logger.info("");
		logger.info("  █▄█ █░█ █▀▀   Yue v" + version);
		logger.info("  ░█░ █▄█ ██▄   by Aeritt");
		logger.info("");
		logger.info("  Logged in as " + jda.getSelfUser().getName());
		logger.info("");
		logger.info("  Saved users: " + personRepository.count());
		logger.info("  Registered commands: " + commandRegistrar.getCommands().size());
		logger.info("  Registered components: " + componentService.getComponentCount());
		logger.info("  Registered translations: " + languageService.getLanguageCount());

		List<String> languages = languageService.getTranslations().keySet().stream().toList();
		String languageCodes = languages.stream()
				.limit(3)
				.collect(Collectors.joining(", "));
		logger.info("  Languages: " + languages.size() + " (" + languageCodes + (languages.size() > 3 ? ", ..." : "") + ")");

		logger.info("");
		logger.info("  Plugins (" + pluginManager.getPlugins().size() + "):");
		pluginManager.getPlugins().forEach(plugin -> logger.info("   - " + plugin.getPluginId() + " v" + plugin.getDescriptor().getVersion()));

		logger.info("");
	}
}
