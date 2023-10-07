package com.spring.boot.blog.app.service;

import com.spring.boot.blog.app.config.JwtService;
import com.spring.boot.blog.app.entity.Role;
import com.spring.boot.blog.app.entity.User;
import com.spring.boot.blog.app.payload.auth.LoginDto;
import com.spring.boot.blog.app.payload.auth.AuthenticationResponse;
import com.spring.boot.blog.app.payload.auth.RegistrationDto;
import com.spring.boot.blog.app.repository.RoleRepository;
import com.spring.boot.blog.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public AuthenticationResponse register(RegistrationDto registrationDto) throws Exception {

        //check if user exists in DB
        if(userRepository.existsByUsernameOrEmail(registrationDto.getUsername(), registrationDto.getEmail())) {
            return AuthenticationResponse.builder().statusCode(HttpStatus.CONFLICT.value()).
                    message("Username or email is already taken").build();
        }

        var user = User.builder().name(registrationDto.getName())
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword())).
                build();


        Role role = roleRepository.findByName("USER").
                orElseThrow(()->new Exception("role USER cannot be fetched from DB"));
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);

        //user.setRoles(Collections.singleton(role));
        user.setRoles(userRoles);

        System.out.println("new user assigned role: " + role);

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().statusCode(HttpStatus.OK.value()).message(HttpStatus.OK.getReasonPhrase()).token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(LoginDto loginDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
                loginDto.getPassword()));
        var user = userRepository.findByUsername(loginDto.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).message(HttpStatus.OK.getReasonPhrase()).
                statusCode(HttpStatus.OK.value()).build();
    }
}
