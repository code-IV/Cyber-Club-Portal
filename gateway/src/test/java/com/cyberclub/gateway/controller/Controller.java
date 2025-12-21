package com.cyberclub.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class Controller {

    @GetMapping("/anything")
    public String ok() {
        return "ok";
    }

    @GetMapping("/forwardtest")
    public ResponseEntity<Void> echo(HttpServletRequest req) {
        return ResponseEntity.ok()
                .header("X-Tenant-Id", req.getHeader("X-Tenant-Id"))
                .header("X-User-Id", req.getHeader("X-User-Id"))
                .header("X-Request-Id", req.getHeader("X-Request-Id"))
                .build();
    }
}
