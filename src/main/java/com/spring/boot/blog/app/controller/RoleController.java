package com.spring.boot.blog.app.controller;

import com.spring.boot.blog.app.payload.RoleDto;
import com.spring.boot.blog.app.payload.SimpleResponse;
import com.spring.boot.blog.app.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/createRole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createRole(@RequestBody RoleDto roleDto) {
        System.out.println("create new role request: " + roleDto);
        SimpleResponse simpleResponse = roleService.create(roleDto);
        return ResponseEntity.status(simpleResponse.getCode()).body(simpleResponse);
    }

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('TEST')")
    public ResponseEntity<?> test(@RequestBody RoleDto roleDto) {
        return ResponseEntity.ok("This is only for TEST");
    }
}
