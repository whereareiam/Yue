package com.aeritt.yue.core.config.configs.setting.database;

import lombok.Getter;

@Getter
public class MySQLDatabaseConfig {
	private String host = "localhost";
	private int port = 3306;
	private String database = "yue";
	private String username = "root";
	private String password = "password";
	private boolean useSSL = false;
	private boolean autoReconnect = true;
}
