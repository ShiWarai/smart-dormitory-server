package ru.mirea.smart_dormitory.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = "ru.mirea.smart_dormitory.repositories")
@EnableAspectJAutoProxy
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
//        sessionFactoryBean.setPackagesToScan("ru.mirea.smart_dormitory");
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