package me.whereareiam.yue.database;

import jakarta.annotation.PostConstruct;
import me.whereareiam.yue.config.setting.SettingsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DatabaseManager {
	private final SettingsConfig settingsConfig;
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public DatabaseManager(SettingsConfig settingsConfig) {
		this.settingsConfig = settingsConfig;
		this.jdbcTemplate = new JdbcTemplate(createDataSource());
	}

	private DataSource createDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		Database database = settingsConfig.getDatabase().getType();
		switch (database) {
			case MYSQL:
				dataSource.setUrl("jdbc:mysql://" + settingsConfig.getDatabase().getHost() + ":" + settingsConfig.getDatabase().getPort() + "/" + settingsConfig.getDatabase().getDatabase() + "?useSSL=" + settingsConfig.getDatabase().isUseSSL() + "&autoReconnect=" + settingsConfig.getDatabase().isAutoReconnect());
				break;
			default:
				throw new RuntimeException("Unsupported database type: " + database);
		}

		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUsername(settingsConfig.getDatabase().getUsername());
		dataSource.setPassword(settingsConfig.getDatabase().getPassword());
		return dataSource;
	}

	@PostConstruct
	public void initialize() {
		createTables();
	}

	private void createTables() {
		String prefix = settingsConfig.getDatabase().getTablePrefix();

		//yue_languages
		jdbcTemplate.execute(
				"CREATE TABLE IF NOT EXISTS " + prefix + "languages (" +
						"id INT AUTO_INCREMENT PRIMARY KEY, " +
						"code VARCHAR(255) NOT NULL, " +
						"name VARCHAR(255) NOT NULL" +
						")"
		);

		//yue_users
		jdbcTemplate.execute(
				"CREATE TABLE IF NOT EXISTS " + prefix + "users (" +
						"id INT AUTO_INCREMENT PRIMARY KEY, " +
						"global_name VARCHAR(255) NOT NULL" +
						"name VARCHAR(255) NOT NULL" +
						")"
		);

		//yue_user_languages
		jdbcTemplate.execute(
				"CREATE TABLE IF NOT EXISTS " + prefix + "user_languages (" +
						"user_id INT NOT NULL, " +
						"language_id INT NOT NULL, " +
						"PRIMARY KEY (user_id, language_id), " +
						"FOREIGN KEY (user_id) REFERENCES " + prefix + "users(id), " +
						"FOREIGN KEY (language_id) REFERENCES " + prefix + "languages(id)" +
						")"
		);
	}
}

