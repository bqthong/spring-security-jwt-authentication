package com.spring.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    
    @GetMapping("/public")
    public String publicAPI() {
        return "Hello public!";
    }
    
    @GetMapping("/private")
    public String privateAPI() {
        return "Hello private!";
    }
    
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminPage() {
        return "Hello ADMIN!";
    }
}


