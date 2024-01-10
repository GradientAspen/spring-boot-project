package com.project.springbootproject.mapper;

import com.project.springbootproject.config.MapperConfig;
import com.project.springbootproject.dto.userDto.UserDto;
import com.project.springbootproject.dto.userDto.UserRequestDto;
import com.project.springbootproject.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserDto toUserDto(User user);

    User toUserModel(UserRequestDto userRequestDto);

}
