package com.spring.boot.blog.app.controller;

import com.spring.boot.blog.app.payload.HttpResponse;
import com.spring.boot.blog.app.payload.auth.LoginDto;
import com.spring.boot.blog.app.payload.auth.AuthenticationResponse;
import com.spring.boot.blog.app.payload.auth.RegistrationDto;
import com.spring.boot.blog.app.service.AuthenticationService;
import com.spring.boot.blog.app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> register(@RequestBody RegistrationDto registrationDto) throws Exception {
        System.out.println("Registration Req: " + registrationDto);
        HttpResponse response = authenticationService.register(registrationDto);
        System.out.println("registration response : " + response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody LoginDto loginDto) {
        System.out.println("Authentication Req: " + loginDto);
        return ResponseEntity.ok(authenticationService.authenticate(loginDto));
    }

    @GetMapping
    public ResponseEntity<HttpResponse> confirmUserAccount(@RequestParam("token") String token) throws Exception {

        System.out.println("\n");
        System.out.println("confirm user acc: " + token);
        System.out.println("\n");

        Boolean isSuccess = userService.verifyConfirmationToken(token);

        System.out.println("\n");
        System.out.println("confirm user acc: isSuccess: " + isSuccess);
        System.out.println("\n");

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("Success", isSuccess))
                        .message("Account Verified")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {

        System.out.println("refresh token req..." );
        authenticationService.refreshToken(httpServletRequest, httpServletResponse);
    }
}
