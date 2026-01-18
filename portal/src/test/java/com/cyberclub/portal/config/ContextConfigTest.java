package com.cyberclub.portal.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.Ordered;

import com.cyberclub.portal.filters.*;


@TestConfiguration
public class ContextConfigTest {

    @Bean
    public FilterRegistrationBean<JwtFilterTest> jwtFilterTest() {
        FilterRegistrationBean<JwtFilterTest> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JwtFilterTest());
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
    
}