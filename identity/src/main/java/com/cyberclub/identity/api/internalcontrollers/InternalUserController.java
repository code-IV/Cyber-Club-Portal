package com.cyberclub.identity.api.internalcontrollers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cyberclub.identity.api.dtos.MemberRecord;
import com.cyberclub.identity.repository.UserRepo;

import java.util.List;

@RestController
@Validated
@RequestMapping("/private/api")
public class InternalUserController{

    private final UserRepo user;

    public InternalUserController(UserRepo user){
        this.user = user;
    }

    @GetMapping("/{serviceName}/members")
    public ResponseEntity<List<MemberRecord>> getAllUsers(@PathVariable String serviceName){
        return ResponseEntity.ok(user.findAll(serviceName));
    }

}
