package com.cyberclub.identity.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.cyberclub.identity.tenancy.TenantDataSource;

@Configuration
public class DataSourceConfig{

    @Bean
    public DataSource physicaDataSource ( DataSourceProperties properties){
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean
    @Primary
    public DataSource dataService(@Qualifier("physicalDataSource") DataSource originalDataSource){
        return new TenantDataSource(originalDataSource);
    }
    
}