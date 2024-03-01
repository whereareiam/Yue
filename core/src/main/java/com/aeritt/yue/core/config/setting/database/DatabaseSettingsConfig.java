package com.aeritt.yue.core.config.setting.database;

import lombok.Getter;
import com.aeritt.yue.core.database.Database;

@Getter
public class DatabaseSettingsConfig {
	private Database type = Database.MYSQL;
	private MySQLDatabaseConfig mysql = new MySQLDatabaseConfig();
	private SQLiteDatabaseConfig sqlite = new SQLiteDatabaseConfig();
}
