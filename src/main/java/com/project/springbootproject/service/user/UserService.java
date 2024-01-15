package com.project.springbootproject.service.user;

import com.project.springbootproject.dto.userdto.UserDto;
import com.project.springbootproject.dto.userdto.UserRequestDto;

public interface UserService {
    UserDto register(UserRequestDto userRequestDto);
}
