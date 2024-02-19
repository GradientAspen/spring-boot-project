package com.project.springbootproject.service.user;

import com.project.springbootproject.dto.userdto.UserDto;
import com.project.springbootproject.dto.userdto.UserRequestDto;
import com.project.springbootproject.exception.RegistrationException;
import com.project.springbootproject.mapper.UserMapper;
import com.project.springbootproject.model.Role;
import com.project.springbootproject.model.ShoppingCart;
import com.project.springbootproject.model.User;
import com.project.springbootproject.repository.role.RoleRepository;
import com.project.springbootproject.repository.shoppingcart.ShoppingCartRepository;
import com.project.springbootproject.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    @Transactional
    public UserDto register(UserRequestDto userRequestDto) {
        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can not register user");
        }

        User user = createUserFromRequest(userRequestDto);
        ShoppingCart shoppingCart = createShoppingCartForUser(user);
        userRepository.save(user);
        shoppingCartRepository.save(shoppingCart);
        return userMapper.toUserDto(user);
    }

    private User createUserFromRequest(UserRequestDto userRequestDto) {
        User user = new User();
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setShippingAddress(userRequestDto.getShippingAddress());
        Role userRole = roleRepository.findByRole(Role.RoleName.USER)
                .orElseThrow(() -> new IllegalStateException("Role User not found"));
        user.setRoles(Collections.singleton(userRole));
        return user;
    }

    private ShoppingCart createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setCartItems(new HashSet<>());
        return shoppingCart;
    }
}
