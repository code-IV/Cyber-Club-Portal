package com.cyberclub.challenge.tenancy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;


import static org.junit.jupiter.api.Assertions.*;

class TenantResolverTest {

    private TenantResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new TenantResolver();
    }

    @Test
    void resolvesTenantFromHost() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Host", "example.tenantA.com");

        String tenant = resolver.resolveTenant(request);

        assertEquals("tenantA", tenant);
    }

    @Test
    void fallsBackToHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Host", "localhost");
        request.addHeader("X-Tenant-Id", "tenantB");

        String tenant = resolver.resolveTenant(request);

        assertEquals("tenantB", tenant);
    }

    @Test
    void TenantCannotBeResolved() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Host", "localhost");

        assertThrows(
            IllegalStateException.class,
            () -> resolver.resolveTenant(request)
        );
    }

    @Test
    void ignoresPortInHostHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Host", "example.tenantA.com:8080");

        String tenant = resolver.resolveTenant(request);

        assertEquals("tenantA", tenant);
    }

    @Test
    void ignoresIpAddressHost() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Host", "127.0.0.1");

        assertThrows(
            IllegalStateException.class,
            () -> resolver.resolveTenant(request)
        );
    }
}
