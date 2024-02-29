package com.aeritt.yue.core;

import org.pf4j.spring.SpringPluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Configuration
public class YueConfig {
	private final ApplicationContext ctx;

	@Autowired
	public YueConfig(ApplicationContext ctx) {
		this.ctx = ctx;
	}

	@Bean
	public SpringPluginManager pluginManager() {
		SpringPluginManager pluginManager = new SpringPluginManager();
		pluginManager.setApplicationContext(ctx);

		return pluginManager;
	}

	@Bean
	@Qualifier("version")
	public String getVersion() {
		String version;
		try {
			version = ctx.getBean(BuildProperties.class).getVersion();
		} catch (Exception e) {
			version = "DEV";
		}

		return version;
	}

	@Bean
	@Primary
	@Qualifier("dataPath")
	public Path getDataPath() {
		return System.getProperty("app.dir") != null ? Paths.get(System.getProperty("app.dir")) : Paths.get("");
	}

	@Bean
	@Qualifier("languagePath")
	public Path getLanguagePath() {
		Path languagePath = ctx.getBean(Path.class).resolve("language");
		if (!languagePath.toFile().exists()) {
			boolean dirCreated = languagePath.toFile().mkdir();
			if (!dirCreated) {
				throw new RuntimeException("Failed to create language directory.");
			}
		}

		return languagePath;
	}

	@Bean
	@Qualifier("pluginPath")
	public Path getPluginPath() {
		Path pluginPath = ctx.getBean(Path.class).resolve("plugins");
		if (!pluginPath.toFile().exists()) {
			boolean dirCreated = pluginPath.toFile().mkdir();
			if (!dirCreated) {
				throw new RuntimeException("Failed to create plugin directory.");
			}
		}

		return pluginPath;
	}

	@Bean
	@Qualifier
	public ApplicationContext getApplicationContext() {
		return new AnnotationConfigApplicationContext("spring");
	}

	@Bean
	public ConfigurableApplicationContext getConfigurableApplicationContext() {
		return (ConfigurableApplicationContext) ctx;
	}

	@Bean
	@Qualifier("yue")
	public Logger getLogger() {
		return Logger.getLogger("Yue");
	}
}
