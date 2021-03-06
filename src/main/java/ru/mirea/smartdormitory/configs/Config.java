package ru.mirea.smartdormitory.configs;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableJpaRepositories(basePackages = "ru.mirea.smartdormitory.repositories")
@ComponentScan(basePackages = {"ru.mirea.smartdormitory"})
@EntityScan("ru.mirea.smartdormitory")
@EnableScheduling
@EnableAsync
public class Config {
}