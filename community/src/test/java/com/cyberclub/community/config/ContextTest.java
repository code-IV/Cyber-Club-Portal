package com.cyberclub.community.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import com.cyberclub.community.filters.JwtFilter;
import com.cyberclub.community.filters.TenantFilter;

@SpringBootApplication(scanBasePackages = {"com.cyberclub.community"})
public class ContextTest{

    @Bean
    public FilterRegistrationBean<JwtFilter> jwFilterTest(JwtFilter jwtFilter){
        FilterRegistrationBean<JwtFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(jwtFilter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<TenantFilter> tenantFilterTest (TenantFilter tenantFilter){
        FilterRegistrationBean<TenantFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(tenantFilter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        return registration;
    }
}