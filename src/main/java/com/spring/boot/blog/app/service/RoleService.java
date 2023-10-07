package com.spring.boot.blog.app.service;

import com.spring.boot.blog.app.entity.Role;
import com.spring.boot.blog.app.payload.RoleDto;
import com.spring.boot.blog.app.payload.SimpleResponse;
import com.spring.boot.blog.app.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public SimpleResponse create(RoleDto roleDto) {
        System.out.println("BAL :  " + roleDto.getName());
        if(roleRepository.existsByName(roleDto.getName())) {
            return SimpleResponse.builder().code(HttpStatus.CONFLICT.value())
                    .message("Role already exists").build();
        }
        Role newRole = new Role();
        newRole.setName(roleDto.getName());
        Role r = roleRepository.save(newRole);

        return SimpleResponse.builder().code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase()).build();
    }
}
