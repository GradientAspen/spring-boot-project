package com.project.springbootproject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequestDto {
    private Long id;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String shippingAddress;
}
