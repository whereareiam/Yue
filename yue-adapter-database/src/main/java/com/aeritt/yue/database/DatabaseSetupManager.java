package com.aeritt.yue.database;

import com.aeritt.yue.api.config.SettingsConfig;
import com.aeritt.yue.api.config.database.DatabaseSettingsConfig;
import com.aeritt.yue.api.exception.DatabaseSetupException;
import com.aeritt.yue.api.util.BeanRegistrationUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.logging.Logger;

@Component
public class DatabaseSetupManager {
	private final ApplicationContext ctx;
	private final Logger logger;
	private final SettingsConfig settingsConfig;
	private final BeanRegistrationUtil beanRegistrationUtil;

	@Autowired
	public DatabaseSetupManager(@Qualifier ApplicationContext ctx, Logger logger, SettingsConfig settingsConfig,
	                            BeanRegistrationUtil beanRegistrationUtil) {
		this.ctx = ctx;
		this.logger = logger;
		this.settingsConfig = settingsConfig;
		this.beanRegistrationUtil = beanRegistrationUtil;
	}

	@PostConstruct
	public void initialize() {
		try {
			createDataSource();
			createEntityManagerFactory();
			createTransactionManager();
		} catch (Exception e) {
			throw new DatabaseSetupException("Failed to set up DatabaseType connection.", e);
		}

		logger.info("DatabaseType connection established.");
	}

	private void createDataSource() {
		HikariConfig hikariConfig = new HikariConfig();
		DatabaseSettingsConfig database = settingsConfig.getDatabase();
		switch (settingsConfig.getDatabase().getType()) {
			case MYSQL:
				hikariConfig.setJdbcUrl("jdbc:mysql://" + database.getMysql().getHost() + ":" + database.getMysql().getPort() + "/" + database.getMysql().getDatabase() + "?useSSL=" + database.getMysql().isUseSSL() + "&autoReconnect=" + database.getMysql().isAutoReconnect());
				hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
				hikariConfig.setUsername(database.getMysql().getUsername());
				hikariConfig.setPassword(database.getMysql().getPassword());
				break;
			case SQLITE:
				hikariConfig.setJdbcUrl("jdbc:sqlite:" + database.getSqlite().getFile());
				hikariConfig.setDriverClassName("org.sqlite.JDBC");
				break;
			default:
				throw new RuntimeException("Unsupported database type: " + database);
		}

		hikariConfig.setPoolName(database.getHikariCP().getPoolName());
		hikariConfig.setMaximumPoolSize(database.getHikariCP().getMaximumPoolSize());
		hikariConfig.setMinimumIdle(database.getHikariCP().getMinimumIdle());
		hikariConfig.setConnectionTimeout(database.getHikariCP().getConnectionTimeout());
		hikariConfig.setIdleTimeout(database.getHikariCP().getIdleTimeout());
		hikariConfig.setMaxLifetime(database.getHikariCP().getMaxLifetime());

		hikariConfig.setLeakDetectionThreshold(60000);
		hikariConfig.setConnectionTestQuery("SELECT 1");

		HikariDataSource dataSource = new HikariDataSource(hikariConfig);
		beanRegistrationUtil.registerSingleton("dataSource", DataSource.class, dataSource);
	}

	private void createEntityManagerFactory() {
		DataSource dataSource = ctx.getBean(DataSource.class);
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

		em.setDataSource(dataSource);
		em.setPackagesToScan("com.aeritt.yue.core.database");
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.hbm2ddl.auto", "update");
		jpaProperties.put("hibernate.connection.characterEncoding", "UTF-8");
		jpaProperties.put("hibernate.connection.CharSet", "UTF-8");
		jpaProperties.put("hibernate.connection.useUnicode", true);

		em.setJpaProperties(jpaProperties);

		em.afterPropertiesSet();

		beanRegistrationUtil.registerSingleton("entityManagerFactory", EntityManagerFactory.class, em.getObject());
	}

	private void createTransactionManager() {
		EntityManagerFactory emf = ctx.getBean(EntityManagerFactory.class);
		JpaTransactionManager tm = new JpaTransactionManager();
		tm.setEntityManagerFactory(emf);

		beanRegistrationUtil.registerSingleton("transactionManager", JpaTransactionManager.class, tm);
	}
}