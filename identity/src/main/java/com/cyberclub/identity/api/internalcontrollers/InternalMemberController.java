package com.cyberclub.identity.api.internalcontrollers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import com.cyberclub.identity.api.dtos.IsMemberResponse;
import com.cyberclub.identity.repository.MembershipRepo;

@RestController
@RequestMapping("/private/api")
public class InternalMemberController{

    private final MembershipRepo member;

    public InternalMemberController(MembershipRepo member){
        this.member = member;
    }

    @GetMapping("/member/check")
    public ResponseEntity<?> checkMembership(@RequestParam UUID userId, @RequestParam String tenantKey){
        if(!member.tenantExists(tenantKey)){
            return ResponseEntity.notFound().build();
        }

        System.out.println(tenantKey + ": " + userId);

        return member
                .findMembership(userId, tenantKey)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> 
                    ResponseEntity.status(403)
                                .body(new IsMemberResponse(false, null)));
        
    }
}