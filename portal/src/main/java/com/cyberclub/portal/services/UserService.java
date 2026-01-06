package com.cyberclub.portal.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import reactor.core.publisher.Mono;
import com.cyberclub.portal.dtos.Member;

@Service
public class UserService {
    private final AuthService auth;
    private final WebClient webClient;

    public UserService(AuthService auth, WebClient webClient){
        this.auth = auth;
        this.webClient = webClient;
    }

    public List<Member> members(){
        auth.requireMember();
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/private/api/portal/members")
                    .build()
                )
                .retrieve()
                .onStatus(
                    status -> status.value() == 403,
                    res -> Mono.error(new RuntimeException("FORBIDDEN"))
                )
                .onStatus(
                    status -> status.value() == 404,
                    res -> Mono.error(new RuntimeException("NOT FOUND"))
                )
                .bodyToFlux(Member.class)
                .collectList()
                .block();
    }
}
