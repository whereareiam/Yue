package com.aeritt.yue.core.config.setting.database;

import com.aeritt.yue.core.database.Database;
import lombok.Getter;

@Getter
public class DatabaseSettingsConfig {
	private Database type = Database.MYSQL;
	private MySQLDatabaseConfig mysql = new MySQLDatabaseConfig();
	private SQLiteDatabaseConfig sqlite = new SQLiteDatabaseConfig();
	private HikariCPConfig hikariCP = new HikariCPConfig();
}
