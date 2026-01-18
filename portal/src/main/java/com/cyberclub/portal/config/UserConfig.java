package com.cyberclub.portal.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import com.cyberclub.portal.filters.*;


@Configuration
public class UserConfig {

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtFilter jwtFilter) {
        FilterRegistrationBean<JwtFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(jwtFilter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<GatewayTrustFilter> tenantFilterRegistration(GatewayTrustFilter gatewayFilter) {
        FilterRegistrationBean<GatewayTrustFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(gatewayFilter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        return registration;
    }
}
