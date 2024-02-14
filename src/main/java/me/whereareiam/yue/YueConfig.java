package me.whereareiam.yue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
	@Qualifier("dataPath")
	public Path getDataPath() {
		return Paths.get("");
	}

	@Bean
	@Qualifier("languagePath")
	public Path languagePath() {
		Path languagePath = Paths.get("").resolve("language");
		if (!languagePath.toFile().exists()) {
			boolean dirCreated = languagePath.toFile().mkdir();
			if (!dirCreated) {
				throw new RuntimeException("Failed to create language directory.");
			}
		}

		return languagePath;
	}

	@Bean
	@Qualifier
	public ApplicationContext getApplicationContext() {
		return new AnnotationConfigApplicationContext("spring");
	}

	@Bean
	@Qualifier("yue")
	public Logger getLogger() {
		return Logger.getLogger("Yue");
	}
}
