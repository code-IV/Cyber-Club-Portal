package com.cyberclub.portal.security;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.*;

import com.cyberclub.portal.filters.GatewayTrustFilter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GatewayTrustFilterTest {

    @Test
    void rejects_missing_secret_header() throws Exception {

        GatewayTrustFilter filter = new GatewayTrustFilter("internal-secret");

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = mock(MockFilterChain.class);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentAsString())
                .contains("Gateway validation required");

        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    void allows_request_with_correct_secret() throws Exception {

        GatewayTrustFilter filter =
                new GatewayTrustFilter("internal-secret");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Internal-Auth", "internal-secret");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = mock(MockFilterChain.class);

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
    }
}
