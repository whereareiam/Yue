package me.whereareiam.yue;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class YueConfig {
	@Bean
	@Qualifier("dataPath")
	public Path getDataPath() {
		return Paths.get("");
	}

	@Bean
	@Qualifier
	public ApplicationContext getApplicationContext() {
		return new AnnotationConfigApplicationContext("spring");
	}
}
