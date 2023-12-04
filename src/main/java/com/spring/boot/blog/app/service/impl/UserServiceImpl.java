package com.spring.boot.blog.app.service.impl;

import com.spring.boot.blog.app.entity.Confirmation;
import com.spring.boot.blog.app.entity.Post;
import com.spring.boot.blog.app.entity.User;
import com.spring.boot.blog.app.payload.PostDto;
import com.spring.boot.blog.app.payload.UserDto;
import com.spring.boot.blog.app.payload.auth.RegistrationDto;
import com.spring.boot.blog.app.repository.ConfirmationRepository;
import com.spring.boot.blog.app.repository.UserRepository;
import com.spring.boot.blog.app.service.EmailService;
import com.spring.boot.blog.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ConfirmationRepository confirmationRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    @Override
    public RegistrationDto createUser(RegistrationDto registrationDto) {
        User user = mapToEntity(registrationDto);
        System.out.println("in UserService(createUser) user: " + user);
        user.setEnabled(false);
        System.out.println("in UserService(createUser) user: " + user);

        User newUser = userRepository.save(user);
        System.out.println("in UserService(createUser) new user: " + newUser);

        Confirmation confirmation = new Confirmation(user);
        confirmationRepository.save(confirmation);

        //send Email to the user with token
        //emailService.sendSimpleMailMessage(user.getName(), user.getEmail(), confirmation.getToken());
        //emailService.sendMimeMessageWithAttachments(user.getName(), user.getEmail(), confirmation.getToken());
        //emailService.sendMimeMessageWithEmbeddedFiles(user.getName(), user.getEmail(), confirmation.getToken());
        emailService.sendHtmlEmail(user.getName(), user.getEmail(), confirmation.getToken());
        return mapToDto(newUser);
    }

    @Override
    public Boolean verifyConfirmationToken(String token) throws Exception {

        System.out.println("\n");
        System.out.println("confirm user acc(in UserService): " + token);
        System.out.println("\n");

        Confirmation confirmation = confirmationRepository.findByToken(token).get();

        System.out.println("\n"+confirmation+"\n");

        User user = userRepository.findByEmail(confirmation.getUser().getEmail())
                .orElseThrow(()->new Exception("Activating user failed"));
        user.setEnabled(true);
        userRepository.save(user);
        //confirmationRepository.delete(confirmation);
        return true;
    }

    @Override
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                ()->  new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found!")
        );


        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

    private User mapToEntity(RegistrationDto registrationDto) {
        System.out.println("In baler conversion: " + registrationDto);
        User user = modelMapper.map(registrationDto, User.class);
        return user;
    }

    private RegistrationDto mapToDto(User user) {
        RegistrationDto dto = modelMapper.map(user, RegistrationDto.class);
        return dto;
    }
}
