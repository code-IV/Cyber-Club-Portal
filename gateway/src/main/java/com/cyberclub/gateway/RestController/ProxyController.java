package com.cyberclub.gateway.RestController;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import com.cyberclub.gateway.Forward;

@RestController
public class ProxyController {

    private final Forward forwardingService;

    public ProxyController(Forward forwardingService) {
        this.forwardingService = forwardingService;
    }

    @RequestMapping("/**")
    public ResponseEntity<byte[]> proxy(HttpServletRequest request) throws IOException {
        String tenantKey = (String) request.getAttribute("tenantId");
        String downstreamUrl = "http://"+ tenantKey+ ":8080"; // example downstream service
        System.out.println(" gteway proxy controller === " + downstreamUrl);
        return forwardingService.forward(request, downstreamUrl);
    }
}
