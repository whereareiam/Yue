package me.whereareiam.yue.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableCaching
@EnableJpaRepositories
@SpringBootApplication
@EntityScan
public class YueApplication extends Yue {
	@Autowired
	public YueApplication(@Qualifier ApplicationContext ctx) {
		super(ctx);

		//TODO Help command
		//TODO Plugin control command
		//TODO Move all features to plugins
	}

	public static void main(String[] args) {
		SpringApplication.run(YueApplication.class, args);
	}
}
