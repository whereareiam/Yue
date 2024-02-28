package me.whereareiam.yue.core.config.configs.setting.database;

import lombok.Getter;
import me.whereareiam.yue.core.database.Database;

@Getter
public class DatabaseSettingsConfig {
	private Database type = Database.MYSQL;
	private MySQLDatabaseConfig mysql = new MySQLDatabaseConfig();
	private SQLiteDatabaseConfig sqlite = new SQLiteDatabaseConfig();
}
