package com.spring.boot.blog.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.boot.blog.app.config.JwtService;
import com.spring.boot.blog.app.entity.Confirmation;
import com.spring.boot.blog.app.entity.Role;
import com.spring.boot.blog.app.entity.User;
import com.spring.boot.blog.app.payload.HttpResponse;
import com.spring.boot.blog.app.payload.auth.LoginDto;
import com.spring.boot.blog.app.payload.auth.AuthenticationResponse;
import com.spring.boot.blog.app.payload.auth.RegistrationDto;
import com.spring.boot.blog.app.repository.ConfirmationRepository;
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
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;

    public HttpResponse register(RegistrationDto registrationDto) throws Exception {

        //check if user exists in DB
        if(userRepository.existsByUsernameOrEmail(registrationDto.getUsername(), registrationDto.getEmail())) {
//            return HttpResponse.builder()
//                    .timeStamp(LocalDateTime.now().toString())
//                    .data(Map.of("user_info", registrationDto))
//                    .message("User creation failed")
//                    .status(HttpStatus.CONFLICT)
//                    .statusCode(HttpStatus.CONFLICT.value())
//                    .build();
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }

        var user = User.builder().name(registrationDto.getName())
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword())).
                build();
        user.setEnabled(false);

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
//        for(String roleName : registrationDto.getRoles()) {
//            Role role = roleRepository.findByName(roleName)
//                    .orElseThrow(() -> new Exception("Role not found: " + roleName));
//            roles.add(role);
//        }
        Role role = roleRepository.findByName("USER")
                .orElseThrow(()->new Exception("USER role not found"));
        user.setRoles(roles);
        userRepository.save(user);
        Confirmation confirmation = new Confirmation(user);
        Confirmation c = confirmationRepository.save(confirmation);
        System.out.println("Confirmation from DB: " + c);
        System.out.println("check: " + user);

        emailService.sendHtmlEmail(user.getName(), user.getEmail(), confirmation.getToken());
        return HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .data(Map.of("new_registered_user", registrationDto))
                .message("User created")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .build();
        //not going to return token and authentication response anymore
        //instead a new http response dto will be return for email activation
        /*
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder().statusCode(HttpStatus.OK.value()).
                message(HttpStatus.OK.getReasonPhrase()).
                accessToken(jwtToken).refreshToken(refreshToken).build();
                */
    }



    public AuthenticationResponse authenticate(LoginDto loginDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
                loginDto.getPassword()));
        var user = userRepository.findByUsername(loginDto.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder().accessToken(jwtToken).
                refreshToken(refreshToken).message(HttpStatus.OK.getReasonPhrase()).
                statusCode(HttpStatus.OK.value()).user(user).build();
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
