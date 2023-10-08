package com.spring.boot.blog.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.boot.blog.app.config.JwtService;
import com.spring.boot.blog.app.entity.Role;
import com.spring.boot.blog.app.entity.User;
import com.spring.boot.blog.app.payload.auth.LoginDto;
import com.spring.boot.blog.app.payload.auth.AuthenticationResponse;
import com.spring.boot.blog.app.payload.auth.RegistrationDto;
import com.spring.boot.blog.app.repository.RoleRepository;
import com.spring.boot.blog.app.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
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


        //assigning to only single role
        /*
        Role role = roleRepository.findByName("USER").
                orElseThrow(()->new Exception("role USER cannot be fetched from DB"));
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);


        //user.setRoles(Collections.singleton(role));
        user.setRoles(userRoles);
        System.out.println("new user assigned role: " + role);

        */

        // Fetch and assign roles
        Set<Role> roles = new HashSet<>();
        for(String roleName : registrationDto.getRoles()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new Exception("Role not found: " + roleName));
            roles.add(role);
        }
        user.setRoles(roles);

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder().statusCode(HttpStatus.OK.value()).
                message(HttpStatus.OK.getReasonPhrase()).
                accessToken(jwtToken).refreshToken(refreshToken).build();
    }

    public AuthenticationResponse authenticate(LoginDto loginDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
                loginDto.getPassword()));
        var user = userRepository.findByUsername(loginDto.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder().accessToken(jwtToken).
                refreshToken(refreshToken).message(HttpStatus.OK.getReasonPhrase()).
                statusCode(HttpStatus.OK.value()).build();
    }

    public void refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        String authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken;
        String username;
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);//extract username from token

        if(username != null) {
            User user = this.userRepository.findByUsername(username).orElseThrow();
            if(jwtService.isTokenValid(refreshToken, user)) {
               var accessToken = jwtService.generateToken(user);
               var authResponse = AuthenticationResponse.builder().refreshToken(refreshToken).
                       accessToken(accessToken).build();
               new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), authResponse);
            }
        }
    }
}
