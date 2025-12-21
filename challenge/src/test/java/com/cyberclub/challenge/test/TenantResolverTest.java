package com.cyberclub.challenge.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.cyberclub.challenge.tenancy.TenantResolver;

import static org.junit.jupiter.api.Assertions.*;

class TenantResolverTest {

    private TenantResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new TenantResolver();
    }

    @Test
    void resolvesTenant() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Tenant-Id", "tenantB");

        String tenant = resolver.resolve(request);

        assertEquals("tenantB", tenant);
    }

    @Test
    void TenantCannotBeResolved() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Host", "localhost");

        assertThrows(
            IllegalStateException.class,
            () -> resolver.resolve(request)
        );
    }

    // @Test
    // void ignoresPortInHostHeader() {
    //     MockHttpServletRequest request = new MockHttpServletRequest();
    //     request.addHeader("Host", "example.tenantA.com:8080");

    //     String tenant = resolver.resolve(request);

    //     assertEquals("tenantA", tenant);
    // }

    @Test
    void ignoresIpAddressHost() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Host", "127.0.0.1");

        assertThrows(
            IllegalStateException.class,
            () -> resolver.resolve(request)
        );
    }
}
