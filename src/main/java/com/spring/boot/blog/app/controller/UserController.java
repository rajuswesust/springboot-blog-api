package com.spring.boot.blog.app.controller;

import com.spring.boot.blog.app.entity.User;
import com.spring.boot.blog.app.payload.HttpResponse;
import com.spring.boot.blog.app.payload.auth.RegistrationDto;
import com.spring.boot.blog.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<HttpResponse> createUser(@RequestBody RegistrationDto registrationDto) {
        registrationDto.setEnabled(false);
        System.out.println("bal: " + registrationDto);
        RegistrationDto newRegisteredUser = userService.createUser(registrationDto);
        System.out.println("new registered user: " + newRegisteredUser);
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("new_registered_user", newRegisteredUser))
                        .message("User created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
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

}
