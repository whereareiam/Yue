package me.whereareiam.yue.config.setting;

import me.whereareiam.yue.database.Database;

public class DatabaseSettingsConfig {
	private Database type = Database.MYSQL;
	private String host = "localhost";
	private int port = 3306;
	private String database = "yue";
	private String username = "root";
	private String password = "password";
	private String tablePrefix = "yue_";
	private boolean useSSL = false;
	private boolean autoReconnect = true;

	public Database getType() {
		return type;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getDatabase() {
		return database;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getTablePrefix() {
		return tablePrefix;
	}

	public boolean isUseSSL() {
		return useSSL;
	}

	public boolean isAutoReconnect() {
		return autoReconnect;
	}
}
