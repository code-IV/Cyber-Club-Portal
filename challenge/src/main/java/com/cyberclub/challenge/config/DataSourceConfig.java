package com.cyberclub.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

import com.cyberclub.challenge.tenancy.TenantDataSource;

@Configuration
public class DataSourceConfig {
    
    @Bean
    @Primary
    public DataSource dataSource(@Qualifier("dataSource") DataSource originalDataSource){
        return new TenantDataSource(originalDataSource);
    }
}
