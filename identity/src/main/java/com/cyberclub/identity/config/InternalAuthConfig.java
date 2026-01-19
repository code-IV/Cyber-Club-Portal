package com.cyberclub.identity.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.cyberclub.identity.filters.InternalAuthFilter;


@Configuration
public class InternalAuthConfig {
    
    @Bean
    public FilterRegistrationBean<InternalAuthFilter> internalAuthFilterRegistration(InternalAuthFilter InternalFilter){
        FilterRegistrationBean<InternalAuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(InternalFilter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
