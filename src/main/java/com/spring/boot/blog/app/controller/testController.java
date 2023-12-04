package com.spring.boot.blog.app.controller;

import com.spring.boot.blog.app.payload.SimpleResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class testController {

    @GetMapping
    public String get(HttpServletRequest httpServletRequest) {
        System.out.println("get request : " + httpServletRequest.getHeader(HttpHeaders.ORIGIN));
        return "this is a response of get request";
    }

    @PostMapping
    public ResponseEntity<?> post(HttpServletRequest httpServletRequest) {
        System.out.println("post : " + httpServletRequest.getHeader(HttpHeaders.ORIGIN));
        return ResponseEntity.ok(SimpleResponse.builder().message("ok").build());
    }
}
