package com.aeritt.yue.util;

import com.aeritt.yue.SpringPluginManager;
import com.aeritt.yue.api.model.Language;
import com.aeritt.yue.api.service.LanguageProvider;
import com.aeritt.yue.api.service.LanguageService;
import com.aeritt.yue.api.service.UserService;
import com.aeritt.yue.command.management.CommandRegistrar;
import com.aeritt.yue.component.ComponentService;
import com.aeritt.yue.event.ApplicationBotStarted;
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
@DependsOn({"discordSetupService", "commandRegistrar"})
public class InfoPrinterUtil {
	private final CommandRegistrar commandRegistrar;
	private final ComponentService componentService;
	private final SpringPluginManager pluginManager;
	private final LanguageProvider languageProvider;
	private final LanguageService languageService;
	private final UserService userService;
	private final String version;
	private final Logger logger;
	private final JDA jda;

	@Autowired
	public InfoPrinterUtil(CommandRegistrar commandRegistrar, ComponentService componentService,
	                       SpringPluginManager pluginManager, LanguageProvider languageProvider, LanguageService languageService, UserService userService, @Qualifier("version") String version, Logger logger,
	                       @Lazy JDA jda) {
		this.commandRegistrar = commandRegistrar;
		this.componentService = componentService;
		this.pluginManager = pluginManager;
		this.languageProvider = languageProvider;
		this.languageService = languageService;
		this.userService = userService;
		this.version = version;
		this.logger = logger;
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
		logger.info("  Saved users: " + userService.getUserCount());
		logger.info("  Registered commands: " + commandRegistrar.getCommands().size());
		logger.info("  Registered components: " + componentService.getComponentCount());
		logger.info("  Registered translations: " + languageProvider.getTranslationCount());

		List<Language> languages = languageService.getLanguages();
		String languageCodes = languages.stream()
				.limit(3)
				.map(Language::getCode)
				.collect(Collectors.joining(", "));
		logger.info("  Languages: " + languages.size() + " (" + languageCodes + (languages.size() > 3 ? ", ..." : "") + ")");

		logger.info("");
		logger.info("  Plugins (" + pluginManager.getStartedPlugins().size() + "):");
		pluginManager.getStartedPlugins().forEach(plugin -> logger.info("   - " + plugin.getPluginId() + " v" + plugin.getDescriptor().getVersion()));

		logger.info("");
	}
}
