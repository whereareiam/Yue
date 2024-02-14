package me.whereareiam.yue.util;

import me.whereareiam.yue.config.setting.SettingsConfig;
import net.dv8tion.jda.api.JDA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@Lazy
public class InfoPrinterUtil {
	private final String version;
	private final Logger logger;
	private final SettingsConfig settingsConfig;
	private final JDA jda;

	@Autowired
	public InfoPrinterUtil(@Qualifier("version") String version, Logger logger, SettingsConfig settingsConfig, JDA jda) {
		this.version = version;
		this.logger = logger;
		this.settingsConfig = settingsConfig;
		this.jda = jda;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void printStartMessage() {
		logger.info("");
		logger.info("  █▄█ █░█ █▀▀   Yue v" + version);
		logger.info("  ░█░ █▄█ ██▄   by whereareiam");
		logger.info("");
		logger.info("  Logged in as " + jda.getSelfUser().getName());
		logger.info("  Guild: " + settingsConfig.getDiscord().getGuildId());
		logger.info("");
	}
}
