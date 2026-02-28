package com.stagelink.config;

import java.util.Arrays;
import java.util.stream.Stream;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariDataSource dataSource(
        @Value("${spring.datasource.url}") String url,
        @Value("${spring.datasource.username}") String username,
        @Value("${spring.datasource.password}") String password,
        @Value("${spring.datasource.driver-class-name:org.postgresql.Driver}") String driverClassName
    ) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }

    @Bean
    public Flyway flyway(
        DataSource dataSource,
        @Value("${spring.flyway.locations:classpath:db/migration}") String[] locations,
        @Value("${spring.flyway.baseline-on-migrate:false}") boolean baselineOnMigrate,
        @Value("${app.flyway.clean-on-start:false}") boolean cleanOnStart
    ) {
        Flyway flyway = Flyway.configure()
            .dataSource(dataSource)
            .locations(Arrays.stream(locations).map(String::trim).toArray(String[]::new))
            .baselineOnMigrate(baselineOnMigrate)
            .cleanDisabled(!cleanOnStart)
            .load();

        if (cleanOnStart) {
            flyway.clean();
        }
        flyway.migrate();

        return flyway;
    }

    @Bean
    public static BeanFactoryPostProcessor entityManagerFactoryDependsOnFlywayPostProcessor() {
        return (ConfigurableListableBeanFactory beanFactory) -> {
            String[] entityManagerFactoryBeans =
                beanFactory.getBeanNamesForType(EntityManagerFactory.class, true, false);

            for (String beanName : entityManagerFactoryBeans) {
                String[] currentDependsOn = beanFactory.getBeanDefinition(beanName).getDependsOn();
                String[] updatedDependsOn = currentDependsOn == null
                    ? new String[]{"flyway"}
                    : Stream.concat(Arrays.stream(currentDependsOn), Stream.of("flyway"))
                        .distinct()
                        .toArray(String[]::new);
                beanFactory.getBeanDefinition(beanName).setDependsOn(updatedDependsOn);
            }
        };
    }
}
