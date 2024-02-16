package me.whereareiam.yue.core.util;

import me.whereareiam.yue.api.event.ApplicationSuccessfullyStarted;
import me.whereareiam.yue.core.database.entity.Language;
import me.whereareiam.yue.core.database.repository.LanguageRepository;
import me.whereareiam.yue.core.database.repository.PersonRepository;
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

@Service
@DependsOn("discordSetupManager")
public class InfoPrinterUtil {
	private final String version;
	private final Logger logger;
	private final PersonRepository personRepository;
	private final LanguageRepository languageRepository;
	private final JDA jda;

	@Autowired
	public InfoPrinterUtil(@Qualifier("version") String version, Logger logger, PersonRepository personRepository,
	                       LanguageRepository languageRepository, @Lazy JDA jda) {
		this.version = version;
		this.logger = logger;
		this.personRepository = personRepository;
		this.languageRepository = languageRepository;
		this.jda = jda;
	}

	@Async
	@EventListener(ApplicationSuccessfullyStarted.class)
	public void printStartMessage() {
		logger.info("");
		logger.info("  █▄█ █░█ █▀▀   Yue v" + version);
		logger.info("  ░█░ █▄█ ██▄   by whereareiam");
		logger.info("");
		logger.info("  Logged in as " + jda.getSelfUser().getName());
		logger.info("  Saved users: " + personRepository.count());
		logger.info("  Guild count: " + jda.getGuilds().size());

		List<Language> languages = languageRepository.findAll();
		String languageCodes = languages.stream()
				.map(Language::getCode)
				.limit(5)
				.collect(Collectors.joining(", "));
		logger.info("  Languages: " + languages.size() + " (" + languageCodes + (languages.size() > 5 ? ", ..." : "") + ")");

		logger.info("");
	}

}
