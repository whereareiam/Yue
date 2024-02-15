package me.whereareiam.yue.core.config.setting;

import lombok.Getter;
import me.whereareiam.yue.core.database.Database;

@Getter
public class DatabaseSettingsConfig {
	private Database type = Database.MYSQL;
	private String host = "localhost";
	private int port = 3306;
	private String database = "yue";
	private String username = "root";
	private String password = "password";
	private boolean useSSL = false;
	private boolean autoReconnect = true;
}
