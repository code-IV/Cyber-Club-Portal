package com.cyberclub.portal.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@TestConfiguration
public class TestFlywayConfig {

    @Bean(initMethod = "migrate")
    public Flyway testFlyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .schemas("portal")
                .createSchemas(true)
                .locations("filesystem:../infra/migrations/service-baseline/portal")
                .load();
    }
}
