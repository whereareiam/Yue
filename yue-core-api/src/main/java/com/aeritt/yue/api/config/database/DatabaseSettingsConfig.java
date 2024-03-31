package com.aeritt.yue.api.config.database;

import com.aeritt.yue.api.type.DatabaseType;
import lombok.Getter;

@Getter
public class DatabaseSettingsConfig {
	private DatabaseType type = DatabaseType.MYSQL;
	private MySQLDatabaseConfig mysql = new MySQLDatabaseConfig();
	private SQLiteDatabaseConfig sqlite = new SQLiteDatabaseConfig();
	private HikariCPConfig hikariCP = new HikariCPConfig();
}
