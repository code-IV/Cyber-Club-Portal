package com.cyberclub.identity.api.internalcontrollers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyberclub.identity.repository.TenantRepo;
import com.cyberclub.identity.repository.UserRepo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/private/api")
public class InternalController {
    
    private final UserRepo userRepo;
    private final TenantRepo tenantRepo;

    public InternalController (TenantRepo tenantRepo, UserRepo userRepo){
        this.tenantRepo = tenantRepo;
        this.userRepo  = userRepo;
    }

    @GetMapping("/{tenantKey}/{id}")
    public ResponseEntity<Map<String, Object>> getTenantAndUser(
            @PathVariable String tenantKey,
            @PathVariable String id) {
            
        UUID userId;
        System.out.println(id);
        try {
            userId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        return tenantRepo.findByKey(tenantKey).flatMap(tenant ->
            userRepo.findById(userId).map(user -> {
                Map<String, Object> response = new HashMap<>();
                response.put("tenant", tenant);
                response.put("user", user);
                return ResponseEntity.ok(response);
            })
        ).orElse(ResponseEntity.notFound().build());
    }

}
    

