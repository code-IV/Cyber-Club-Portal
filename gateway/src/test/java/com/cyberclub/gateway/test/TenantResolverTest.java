package com.cyberclub.gateway.test;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.cyberclub.gateway.tenancy.TenantResolver;

import static org.junit.jupiter.api.Assertions.*;

class TenantResolverTest {

    private final TenantResolver resolver = new TenantResolver();

    private HttpServletRequest mockReq(String host, String header) {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Host")).thenReturn(host);
        Mockito.when(req.getHeader("X-Tenant-Id")).thenReturn(header);
        return req;
    }

    @Test
    void resolvesFromSubdomain() {
        HttpServletRequest req = mockReq("tenantA.example.com", null);
        assertEquals("tenanta", resolver.resolveTenant(req));
    }

    @Test
    void fallsBackToHeader() {
        HttpServletRequest req = mockReq(null, "clubX");
        assertEquals("clubx", resolver.resolveTenant(req));
    }

    @Test
    void rejectsInvalidTenant() {
        HttpServletRequest req = mockReq("bad!!.domain.com", null);
        assertThrows(IllegalArgumentException.class, () -> resolver.resolveTenant(req));
    }
}
