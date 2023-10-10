package com.spring.boot.blog.app.payload.auth;

import com.spring.boot.blog.app.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationDto {
    private String name;
    private String email;
    private String username;
    private String password;
    private Set<String> roles;

    private boolean isEnabled;
}
