package com.cyberclub.challenge.restcontrollers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class Home{
    @GetMapping("/")
    public String home(){
        return "HOME";
    }
}
