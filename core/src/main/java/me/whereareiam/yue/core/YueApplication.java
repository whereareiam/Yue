package me.whereareiam.yue.core;

import me.whereareiam.yue.api.Yue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableCaching
@EnableJpaRepositories
@SpringBootApplication
public class YueApplication implements Yue {
	private static ApplicationContext ctx;

	@Autowired
	public YueApplication(@Qualifier ApplicationContext ctx) {
		YueApplication.ctx = ctx;
	}

	public static void main(String[] args) {
		SpringApplication.run(YueApplication.class, args);
	}
}
