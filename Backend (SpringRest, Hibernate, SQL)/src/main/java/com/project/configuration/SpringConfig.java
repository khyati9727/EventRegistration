package com.project.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.project.dao", "com.project.service", "com.project.api"})
public class SpringConfig {
	
	@Bean
	public DataSource dataSource() {

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/eventregistration");     //ADD THE URL of DB HERE
		dataSource.setUsername("root");											//ADD THE USERNAME of DB HERE
		dataSource.setPassword("root");											//ADD THE PASSWORD of DB HERE
		return dataSource;
	}
	

	@Bean
	public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {

		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();

		/**
		 * The received dataSource object which has database information is set
		 * to sessionFactoryBean
		 */
		sessionFactoryBean.setDataSource(dataSource);

		/**
		 * The below code is to set the package in which Entity classes (the
		 * classes to which Database tables are mapped) are present to
		 * sessionFactoryBean
		 */
		sessionFactoryBean.setPackagesToScan("com.project.entity");

		/**
		 * The below code is to create and populate Properties object with
		 * Hibernate properties. Hibernate property values are taken from
		 * database.properties file using environment variable.
		 * 
		 */
		Properties hibernateProperties = new Properties();

		hibernateProperties.setProperty("hibernate.dialect",
				"org.hibernate.dialect.MySQLDialect");
		hibernateProperties.setProperty("hibernate.show_sql", "true");
		 hibernateProperties.setProperty("hibernate.format_sql", "true");

		/**
		 * In the below statement hibernateProperties object created above is
		 * set to sessionFactoryBean
		 */
		sessionFactoryBean.setHibernateProperties(hibernateProperties);

		return sessionFactoryBean;
	}

	/**
	 * This method is to create and populate TransactionManager object with
	 * sessionFactory and dataSource. This transactionManager object is to used
	 * in Spring's annotation-driven transaction management.
	 * 
	 * The created object becomes a bean class for spring container to manage as
	 * this method is annotated with @Bean annotation.
	 * 
	 * @return transactionManager object
	 */

	@Bean
	public PlatformTransactionManager transactionManager(
			SessionFactory sessionFactory, DataSource dataSource) {
		/*
		 * In the below code transactionManager object is created and the
		 * received sessionFactory and dataSource objects are set to
		 * transactionManager object.
		 */

		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory);
		transactionManager.setDataSource(dataSource);
		return transactionManager;
	}

}
