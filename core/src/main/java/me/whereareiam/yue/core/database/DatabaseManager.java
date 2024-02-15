package me.whereareiam.yue.core.database;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import me.whereareiam.yue.core.config.setting.SettingsConfig;
import me.whereareiam.yue.core.util.BeanRegistrationUtil;
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
@DependsOn("configService")
public class DatabaseManager {
	private final ApplicationContext ctx;
	private final BeanRegistrationUtil beanRegistrationUtil;
	private final Logger logger;
	private final SettingsConfig settingsConfig;

	@Autowired
	public DatabaseManager(@Qualifier ApplicationContext ctx, BeanRegistrationUtil beanRegistrationUtil, Logger logger,
	                       SettingsConfig settingsConfig) {
		this.ctx = ctx;
		this.beanRegistrationUtil = beanRegistrationUtil;
		this.logger = logger;
		this.settingsConfig = settingsConfig;
	}

	@PostConstruct
	public void initialize() {
		try {
			createDataSource();
			createEntityManagerFactory();
			createTransactionManager();
		} catch (Exception e) {
			logger.severe("Failed to set up Database connection. Check your settings.json file.");
			System.exit(1);
		}

		logger.info("Database connection established.");
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

		beanRegistrationUtil.registerSingleton("dataSource", DataSource.class, dataSource);
	}

	private void createEntityManagerFactory() {
		DataSource dataSource = ctx.getBean(DataSource.class);
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

		em.setDataSource(dataSource);
		em.setPackagesToScan("me.whereareiam.yue.core.database");
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