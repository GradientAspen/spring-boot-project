package com.project.springbootproject.dto.userdto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String shippingAddress;
}
