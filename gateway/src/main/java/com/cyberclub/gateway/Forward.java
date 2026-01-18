package com.cyberclub.gateway;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
public class Forward {

    private final WebClient webClient = WebClient.builder()
            // Set a higher limit for memory buffer if you expect large payloads
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024)) 
            .build();

    public ResponseEntity<byte[]> forward(HttpServletRequest request, String downstreamUrl) throws IOException {
        
        // 1. Construct the full URL
        String url = downstreamUrl + request.getRequestURI();
        if (request.getQueryString() != null) {
            url += "?" + request.getQueryString();
        }

        // 2. Map Incoming Headers to WebClient Headers
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(name -> 
            headers.add(name, request.getHeader(name))
        );

        // 3. Add Enrichment Headers
        headers.set("X-Service-Name", (String) request.getAttribute("serviceName"));
        headers.set("X-User-Id", (String) request.getAttribute("userId"));
        headers.set("X-Request-Id", UUID.randomUUID().toString());

        // 4. Read the Body
        byte[] body = request.getInputStream().readAllBytes();

        // 5. Execute Request and block to return ResponseEntity
        // (Note: Since your controller returns ResponseEntity<byte[]>, we use .block() here)
        return webClient.method(HttpMethod.valueOf(request.getMethod()))
                .uri(url)
                .headers(h -> h.addAll(headers))
                .bodyValue(body)
                .exchangeToMono(this::handleResponse)
                .block(); 
    }

    private Mono<ResponseEntity<byte[]>> handleResponse(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(byte[].class)
                .defaultIfEmpty(new byte[0])
                .map(body -> ResponseEntity
                        .status(clientResponse.statusCode())
                        .headers(clientResponse.headers().asHttpHeaders())
                        .body(body)
                );
    }
}