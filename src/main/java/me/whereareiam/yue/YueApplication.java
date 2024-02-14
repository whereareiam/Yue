package me.whereareiam.yue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class YueApplication {
	private static ApplicationContext ctx;

	@Autowired
	public YueApplication(@Qualifier ApplicationContext ctx) {
		YueApplication.ctx = ctx;
	}

	public static void main(String[] args) {
		SpringApplication.run(YueApplication.class, args);
	}
}
