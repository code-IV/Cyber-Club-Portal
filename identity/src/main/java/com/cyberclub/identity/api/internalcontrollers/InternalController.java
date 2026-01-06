package com.cyberclub.identity.api.internalcontrollers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyberclub.identity.repository.ServiceRepo;
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
    private final ServiceRepo serviceRepo;

    public InternalController (ServiceRepo serviceRepo, UserRepo userRepo){
        this.serviceRepo = serviceRepo;
        this.userRepo  = userRepo;
    }

    @GetMapping("/{serviceName}/{id}")
    public ResponseEntity<Map<String, Object>> getServiceAndUser(
            @PathVariable String serviceName,
            @PathVariable String id) {
            
        UUID userId;
        
        try {
            userId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        return serviceRepo.findByName(serviceName).flatMap(service ->
            userRepo.findById(userId).map(user -> {
                Map<String, Object> response = new HashMap<>();
                response.put("service", service);
                response.put("user", user);
                return ResponseEntity.ok(response);
            })
        ).orElse(ResponseEntity.notFound().build());
    }

}
    

