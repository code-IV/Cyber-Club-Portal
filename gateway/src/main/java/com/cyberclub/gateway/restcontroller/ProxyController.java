package com.cyberclub.gateway.restcontroller;

import java.io.IOException;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import com.cyberclub.gateway.Forward;

@RestController
public class ProxyController {

    private final Forward forwardingService;
    private static final Set<String> ALLOWED_SERVICES = Set.of("identity", "portal", "learn", "challenge", "community");

    public ProxyController(Forward forwardingService) {
        this.forwardingService = forwardingService;
    }

    @RequestMapping("/**")
    public ResponseEntity<byte[]> proxy(HttpServletRequest request) throws IOException {
        String service = (String) request.getAttribute("serviceName");
        
        if (service == null || service.isEmpty()) {
            throw new IllegalArgumentException("serviceName attribute is required");
        }
        
        // Validate against whitelist to prevent SSRF
        if (!ALLOWED_SERVICES.contains(service)) {
            throw new IllegalArgumentException("Invalid service name: " + service);
        }
        
        String downstreamUrl = "http://"+ service + ":8080"; // Use HTTPS for encryption
        return forwardingService.forward(request, downstreamUrl);
    }

}
