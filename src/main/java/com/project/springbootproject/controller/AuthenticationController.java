package com.project.springbootproject.controller;

import com.project.springbootproject.dto.userDto.UserDto;
import com.project.springbootproject.dto.userDto.UserRequestDto;
import com.project.springbootproject.exception.RegistrationException;
import com.project.springbootproject.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/registration")
    public UserDto registerUser(@RequestBody UserRequestDto userRequestDto) throws RegistrationException {
        return userService.register(userRequestDto);
    }

}
