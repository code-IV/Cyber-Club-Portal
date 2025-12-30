package com.cyberclub.gateway;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class Forward {

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<byte[]> forward(HttpServletRequest request, String downstreamUrl) throws IOException {
        // Build the URL including path and query params
        String url = downstreamUrl + request.getRequestURI();
        if (request.getQueryString() != null) {
            url += "?" + request.getQueryString();
        }

        // Copy headers
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.add(name, request.getHeader(name));
        }

        // Ensure enriched headers exist
        headers.set("X-Tenant-Id", (String) request.getAttribute("tenantId"));
        headers.set("X-User-Id", (String) request.getAttribute("userId"));
        headers.set("X-Request-Id", UUID.randomUUID().toString());


        // Copy body
        InputStream inputStream = request.getInputStream();
        byte[] body = inputStream.readAllBytes();

        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        HttpEntity<byte[]> httpEntity = new HttpEntity<>(body, headers);


        // Send request
        try {
            return restTemplate.exchange(url, method, httpEntity, byte[].class);
        } catch (RestClientException e) {
            // Log the error with request context
            throw new RuntimeException("Failed to forward request to " + downstreamUrl, e);
        }
    }
}
