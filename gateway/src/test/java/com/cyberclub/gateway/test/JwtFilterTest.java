package com.cyberclub.gateway.test;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.cyberclub.gateway.filters.JwtFilter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class JwtFilterTest {

    private final JwtFilter filter = new JwtFilter();

    @Test
    void acceptsFakeUserToken() throws Exception {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse res = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);

        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer user-123");

        assertDoesNotThrow(() -> filter.doFilter(req, res, chain));
    }

    @Test
    void ignoresMissingToken() throws Exception {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse res = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);

        Mockito.when(req.getHeader("Authorization")).thenReturn(null);

        assertDoesNotThrow(() -> filter.doFilter(req, res, chain));
    }
}
