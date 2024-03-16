package com.aeritt.yue.core.config.setting.database;

import lombok.Getter;

@Getter
public class HikariCPConfig {
	private String poolName = "YuePool";
	private int maximumPoolSize = 10;
	private int minimumIdle = 5;
	private long connectionTimeout = 30000;
	private long idleTimeout = 600000;
	private long maxLifetime = 1800000;
}
