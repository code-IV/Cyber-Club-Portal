package com.cyberclub.challenge.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

import com.cyberclub.challenge.tenancy.TenantDataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource physicalDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }
    
    @Bean
    @Primary
    public DataSource dataService(@Qualifier("physicalDataSource") DataSource originalDataSource){
        return new TenantDataSource(originalDataSource);
    }
}
