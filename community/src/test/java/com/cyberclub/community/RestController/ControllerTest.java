package com.cyberclub.community.RestController;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyberclub.community.context.TenantContext;
import com.cyberclub.community.context.UserContext;

@RestController
public class ControllerTest {

    @GetMapping("/test/context")
    public ResponseEntity<Map<String, String>> getContext(){
        if(!UserContext.isSet()){
            return ResponseEntity.status(401).body(Map.of("error", "no user in Context"));
        }

        return ResponseEntity.ok(
            Map.of(
                "tenant" , TenantContext.get(),
                "userId", UserContext.get()
            )
        );
    }
}
