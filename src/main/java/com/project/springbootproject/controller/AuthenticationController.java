package com.project.springbootproject.controller;

import com.project.springbootproject.dto.userdto.UserDto;
import com.project.springbootproject.dto.userdto.UserLoginRequestDto;
import com.project.springbootproject.dto.userdto.UserLoginResponseDto;
import com.project.springbootproject.dto.userdto.UserRequestDto;
import com.project.springbootproject.security.AuthenticationService;
import com.project.springbootproject.service.user.UserService;
import jakarta.validation.Valid;
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
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public UserDto registerUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        return userService.register(userRequestDto);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
