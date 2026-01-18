package com.cyberclub.portal.security;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.cyberclub.portal.dtos.AuthResult;
import com.cyberclub.portal.exceptions.ForbiddenException;
import com.cyberclub.portal.exceptions.NotFoundException;

import reactor.core.publisher.Mono;

@Component
public class ClientAuth {
    
    private final WebClient webClient;

    public ClientAuth(WebClient webClient){
        this.webClient = webClient;
    }

    public AuthResult checkMembership( UUID id){
        return webClient
                .get()
                .uri( uriBuilder -> uriBuilder
                    .path("/private/api/member/check")
                    .queryParam("userId", id)
                    .queryParam("serviceName", "portal")
                    .build()
                )
                .retrieve()
                .onStatus(
                    status -> status.value() == 403, 
                    res -> Mono.error( new ForbiddenException("FORBIDDEN"))
                )
                .onStatus(
                    status -> status.value() == 404,
                    res -> Mono.error(new NotFoundException("NOT_FOUND"))
                )
                .onStatus(
                    status -> status.is5xxServerError(),
                    res -> Mono.error(new RuntimeException("Service_UnAvailable"))
                )
                .bodyToMono(AuthResult.class)
                .block();
    }
}
