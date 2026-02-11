package com.cyberclub.learn.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.cyberclub.learn.filters.GatewayTrustFilter;
import com.cyberclub.learn.filters.JwtFilter;

@Configuration
public class UserConfig {
    
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtFilter jwtFilter){
        FilterRegistrationBean<JwtFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(jwtFilter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<GatewayTrustFilter> gatewayTrFilterRegistration(GatewayTrustFilter gatewayTrustFilter){
        FilterRegistrationBean<GatewayTrustFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(gatewayTrustFilter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

}
