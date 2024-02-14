package me.whereareiam.yue.database;

import jakarta.annotation.PostConstruct;
import me.whereareiam.yue.config.setting.SettingsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DatabaseManager {
	private final ConfigurableApplicationContext context;
	private final SettingsConfig settingsConfig;

	@Autowired
	public DatabaseManager(ConfigurableApplicationContext context, SettingsConfig settingsConfig) {
		this.context = context;
		this.settingsConfig = settingsConfig;
	}

	private void createDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		Database database = settingsConfig.getDatabase().getType();
		switch (database) {
			case MYSQL:
				dataSource.setUrl("jdbc:mysql://" + settingsConfig.getDatabase().getHost() + ":" + settingsConfig.getDatabase().getPort() + "/" + settingsConfig.getDatabase().getDatabase() + "?useSSL=" + settingsConfig.getDatabase().isUseSSL() + "&autoReconnect=" + settingsConfig.getDatabase().isAutoReconnect());
				dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
				break;
			default:
				throw new RuntimeException("Unsupported database type: " + database);
		}

		dataSource.setUsername(settingsConfig.getDatabase().getUsername());
		dataSource.setPassword(settingsConfig.getDatabase().getPassword());

		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClass(DataSource.class);
		beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
		beanDefinition.setLazyInit(false);
		beanDefinition.setAutowireCandidate(true);
		context.getBeanFactory().registerSingleton("dataSource", dataSource);
	}

	@PostConstruct
	public void initialize() {
		createDataSource();
	}
}