package com.spring.boot.blog.app.controller;

import com.spring.boot.blog.app.payload.auth.LoginDto;
import com.spring.boot.blog.app.payload.auth.AuthenticationResponse;
import com.spring.boot.blog.app.payload.auth.RegistrationDto;
import com.spring.boot.blog.app.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationDto registrationDto)
            throws Exception {
        System.out.println("Registration Req: " + registrationDto);
        AuthenticationResponse result = authenticationService.register(registrationDto);
        System.out.println("registration response : " + result);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody LoginDto loginDto) {
        System.out.println("Authentication Req: " + loginDto);
        return ResponseEntity.ok(authenticationService.authenticate(loginDto));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {

        System.out.println("refresh token req..." );
        authenticationService.refreshToken(httpServletRequest, httpServletResponse);
    }
}
