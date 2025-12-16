package com.cyberclub.challenge.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import com.cyberclub.challenge.tenancy.TenantFilter;
import com.cyberclub.challenge.security.JWTfilter;


@Configuration
public class UserConfig {

    @Bean
    public FilterRegistrationBean<JWTfilter> jwtFilter(JWTfilter jwtFilter) {
        FilterRegistrationBean<JWTfilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(jwtFilter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<TenantFilter> tenantFilter(TenantFilter tenantFilter) {
        FilterRegistrationBean<TenantFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(tenantFilter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        return registration;
    }
}
