package me.whereareiam.yue.config.setting;

import lombok.Getter;
import me.whereareiam.yue.database.Database;

@Getter
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
}
