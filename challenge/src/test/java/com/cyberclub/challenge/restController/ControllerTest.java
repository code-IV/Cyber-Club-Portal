package com.cyberclub.challenge.restController;

import org.springframework.web.bind.annotation.RestController;

import com.cyberclub.challenge.context.TenantContext;
import com.cyberclub.challenge.context.UserContext;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class ControllerTest {

    @GetMapping("/test/context")
    public ResponseEntity<Map<String, String>> getContext() {

        if (!UserContext.isSetUserId()) {
        return ResponseEntity.status(401)
                .body(Map.of("error", "no user in context"));
    }

        return ResponseEntity.ok(
            Map.of(
            "tenant", TenantContext.get(),
            "userId", UserContext.getUserId()
        ));
    }
    
}
