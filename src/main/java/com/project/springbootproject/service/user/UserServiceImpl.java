package com.project.springbootproject.service.user;

import com.project.springbootproject.dto.userdto.UserDto;
import com.project.springbootproject.dto.userdto.UserRequestDto;
import com.project.springbootproject.exception.RegistrationException;
import com.project.springbootproject.mapper.UserMapper;
import com.project.springbootproject.model.User;
import com.project.springbootproject.repository.user.UserRepository;
import com.project.springbootproject.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto register(UserRequestDto userRequestDto) {
        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can not register user");
        }

        User user = new User();
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setShippingAddress(userRequestDto.getShippingAddress());
        User saveUser = userRepository.save(user);
        return userMapper.toUserDto(saveUser);
    }
}
