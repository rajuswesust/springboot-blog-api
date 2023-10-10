package com.spring.boot.blog.app.service;

import com.spring.boot.blog.app.payload.auth.RegistrationDto;

public interface UserService {
    RegistrationDto createUser(RegistrationDto registrationDto);
    Boolean verifyConfirmationToken(String token) throws Exception;
}
