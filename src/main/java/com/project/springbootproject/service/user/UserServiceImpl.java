package com.project.springbootproject.service.user;

import com.project.springbootproject.dto.userDto.UserDto;
import com.project.springbootproject.dto.userDto.UserRequestDto;
import com.project.springbootproject.exception.RegistrationException;
import com.project.springbootproject.mapper.UserMapper;
import com.project.springbootproject.model.User;
import com.project.springbootproject.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto register(UserRequestDto userRequestDto) throws RegistrationException {
        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can not register user");
        }

        User user = new User();
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        User saveUser = userRepository.save(user);
        return userMapper.toUserDto(saveUser);
    }
}
