package com.cyberclub.gateway.restcontroller;

import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import com.cyberclub.gateway.Forward;

@RestController
public class ProxyController {

    private final Logger log = LoggerFactory.getLogger(ProxyController.class);
    private final Forward forwardingService;
    private static final Set<String> SERVICES = Set.of("identity", "portal", "learn", "challenge", "community");

    public ProxyController(Forward forwardingService) {
        this.forwardingService = forwardingService;
    }

    @RequestMapping("/**")
    public ResponseEntity<byte[]> proxy(HttpServletRequest request) throws IOException {
        boolean authenticated = Boolean.TRUE.equals(request.getAttribute("authenticated"));
        String service = (String) request.getAttribute("serviceName");

        if(!authenticated){
            service = "identity";
        } 
        if (!SERVICES.contains(service)) {
            throw new IllegalArgumentException("unidentified service");
        } 

        log.info("auth={}, service={}",
            authenticated,
            service
        );

        
        String downstreamUrl = "http://"+ service + ":8080"; // Use HTTPS for encryption
        return forwardingService.forward(request, downstreamUrl);
    }

}
