package com.aeritt.yue.core.database;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import com.aeritt.yue.api.util.BeanRegistrationUtil;
import com.aeritt.yue.core.config.setting.SettingsConfig;
import com.aeritt.yue.core.config.setting.database.DatabaseSettingsConfig;
import com.aeritt.yue.core.exception.DatabaseSetupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.logging.Logger;

@Component
@DependsOn("configInitializer")
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
			throw new DatabaseSetupException("Failed to set up Database connection.", e);
		}

		logger.info("Database connection established.");
	}

	private void createDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		DatabaseSettingsConfig database = settingsConfig.getDatabase();
		switch (settingsConfig.getDatabase().getType()) {
			case MYSQL:
				dataSource.setUrl("jdbc:mysql://" + database.getMysql().getHost() + ":" + database.getMysql().getPort() + "/" + database.getMysql().getDatabase() + "?useSSL=" + database.getMysql().isUseSSL() + "&autoReconnect=" + database.getMysql().isAutoReconnect());
				dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
				dataSource.setUsername(database.getMysql().getUsername());
				dataSource.setPassword(database.getMysql().getPassword());
				break;
			case SQLITE:
				dataSource.setUrl("jdbc:sqlite:" + database.getSqlite().getFile());
				dataSource.setDriverClassName("org.sqlite.JDBC");
				break;
			default:
				throw new RuntimeException("Unsupported database type: " + database);
		}

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