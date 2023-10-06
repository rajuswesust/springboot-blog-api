package com.spring.boot.blog.app.controller;

import com.spring.boot.blog.app.payload.auth.AuthDto;
import com.spring.boot.blog.app.payload.auth.AuthenticationResponse;
import com.spring.boot.blog.app.payload.auth.RegisterDto;
import com.spring.boot.blog.app.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterDto registerDto) {
        System.out.println("Registration Req: " + registerDto);
        return ResponseEntity.ok(authenticationService.register(registerDto));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthDto authDto) {
        System.out.println("Authentication Req: " + authDto);
        return ResponseEntity.ok(authenticationService.authenticate(authDto));
    }
}
