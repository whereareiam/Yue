package me.whereareiam.yue.core.config.setting;

import lombok.Getter;

@Getter
public class RedisSettingsConfig {
	private String host = "localhost";
	private int port = 6379;
	private String username = "";
	private String password = "";
}
