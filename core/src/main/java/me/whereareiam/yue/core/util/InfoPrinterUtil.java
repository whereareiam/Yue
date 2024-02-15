package me.whereareiam.yue.core.util;

import me.whereareiam.yue.api.event.ApplicationSuccessfullyStarted;
import me.whereareiam.yue.core.database.service.PersonService;
import net.dv8tion.jda.api.JDA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@DependsOn("discordBot")
public class InfoPrinterUtil {
	private final String version;
	private final Logger logger;
	private final PersonService personService;
	private final JDA jda;

	@Autowired
	public InfoPrinterUtil(@Qualifier("version") String version, Logger logger,
	                       PersonService personService, @Lazy JDA jda) {
		this.version = version;
		this.logger = logger;
		this.personService = personService;
		this.jda = jda;
	}

	@EventListener(ApplicationSuccessfullyStarted.class)
	public void printStartMessage() {
		logger.info("");
		logger.info("  █▄█ █░█ █▀▀   Yue v" + version);
		logger.info("  ░█░ █▄█ ██▄   by whereareiam");
		logger.info("");
		logger.info("  Logged in as " + jda.getSelfUser().getName());
		logger.info("  Saved users: " + personService.getUserCount());
		logger.info("  Guild count: " + jda.getGuilds().size());
		logger.info("");
	}
}
