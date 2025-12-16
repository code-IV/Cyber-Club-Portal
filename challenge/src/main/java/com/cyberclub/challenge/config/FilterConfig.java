package com.cyberclub.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.cyberclub.challenge.tenancy.TenantResolver;
import com.cyberclub.challenge.tenancy.TenantFilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;


@Configuration
public class FilterConfig {
    
    @Bean
    public TenantResolver tenantResolver (){
        return new TenantResolver();
    }

    @Bean
    public FilterRegistrationBean<TenantFilter> tenantFilter(TenantResolver tenantResolver){
        FilterRegistrationBean<TenantFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TenantFilter(tenantResolver));
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        return registration;

    }
}
