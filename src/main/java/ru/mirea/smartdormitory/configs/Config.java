package ru.mirea.smartdormitory.configs;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableJpaRepositories(basePackages = "ru.mirea.smartdormitory.model.repositories")
@ComponentScan(basePackages = {"ru.mirea.smartdormitory"})
@EntityScan("ru.mirea.smartdormitory")
@EnableScheduling
@EnableAsync
public class Config {
//
//    @Bean
//    public HikariDataSource dataSource() {
//        HikariConfig config = new HikariConfig();
//        config.setDriverClassName("org.postgresql.Driver");
//        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
//        config.setUsername("shiwarai");
//        config.setPassword("shiwarai");
//        return new HikariDataSource(config);
//    }
//
//    @Bean
//    public LocalSessionFactoryBean sessionFactoryBean(DataSource dataSource) {
//        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
//        sessionFactoryBean.setDataSource(dataSource);
//
//        sessionFactoryBean.setPackagesToScan("ru.mirea.smartdormitory");
//        Properties properties = new Properties();
//        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
//        sessionFactoryBean.setHibernateProperties(properties);
//        return sessionFactoryBean;
//    }
//
//    @Bean
//    public PlatformTransactionManager platformTransactionManager(LocalSessionFactoryBean factoryBean) {
//        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//        transactionManager.setSessionFactory(factoryBean.getObject());
//        return transactionManager;
//    }
}