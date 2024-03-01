package com.aeritt.yue.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EntityScan
@EnableAsync
@EnableCaching
@EnableJpaRepositories
@SpringBootApplication
public class YueApplication {
	public static void main(String[] args) {
		SpringApplication.run(YueApplication.class, args);
	}
}
