package com.aeritt.yue.database;

import com.aeritt.yue.api.config.SettingsConfig;
import com.aeritt.yue.api.config.database.DatabaseSettingsConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
@EntityScan
public class DatabaseConfiguration {
	private final ApplicationContext ctx;
	private final SettingsConfig settingsConfig;

	@Autowired
	public DatabaseConfiguration(@Qualifier ApplicationContext ctx, SettingsConfig settingsConfig) {
		this.ctx = ctx;
		this.settingsConfig = settingsConfig;
	}

	@Bean
	@Primary
	public DataSource getDataSource() {
		HikariConfig hikariConfig = new HikariConfig();
		DatabaseSettingsConfig database = settingsConfig.getDatabase();
		HikariDataSource dataSource;

		try {
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
					throw new RuntimeException("Unsupported internal type: " + database);
			}

			hikariConfig.setPoolName(database.getHikariCP().getPoolName());
			hikariConfig.setMaximumPoolSize(database.getHikariCP().getMaximumPoolSize());
			hikariConfig.setMinimumIdle(database.getHikariCP().getMinimumIdle());
			hikariConfig.setConnectionTimeout(database.getHikariCP().getConnectionTimeout());
			hikariConfig.setIdleTimeout(database.getHikariCP().getIdleTimeout());
			hikariConfig.setMaxLifetime(database.getHikariCP().getMaxLifetime());

			hikariConfig.setLeakDetectionThreshold(60000);
			hikariConfig.setConnectionTestQuery("SELECT 1");

			dataSource = new HikariDataSource(hikariConfig);
			dataSource.getConnection().close();
		} catch (Exception e) {
			throw new RuntimeException("Failed to establish database connection", e);
		}

		return dataSource;
	}

	@Bean(name = "entityManagerFactory")
	public EntityManagerFactory getEntityManagerFactory() {
		DataSource dataSource = ctx.getBean(DataSource.class);

		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

		em.setDataSource(dataSource);
		em.setPackagesToScan(this.getClass().getPackage().getName());
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.hbm2ddl.auto", "update");
		jpaProperties.put("hibernate.connection.characterEncoding", "UTF-8");
		jpaProperties.put("hibernate.connection.CharSet", "UTF-8");
		jpaProperties.put("hibernate.connection.useUnicode", true);

		em.setJpaProperties(jpaProperties);

		em.afterPropertiesSet();

		return em.getObject();
	}

	@Bean(name = "transactionManager")
	public JpaTransactionManager getTransactionManager() {
		EntityManagerFactory emf = ctx.getBean(EntityManagerFactory.class);
		JpaTransactionManager tm = new JpaTransactionManager();
		tm.setEntityManagerFactory(emf);

		return tm;
	}
}