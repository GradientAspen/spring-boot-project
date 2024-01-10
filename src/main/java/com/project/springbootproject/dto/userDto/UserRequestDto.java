package com.project.springbootproject.dto.userDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserRequestDto {
    private Long id;
    @NotNull
    private String email;
    @NotNull
    @Length(min = 8, max = 20)
    private String password;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String shippingAddress;
}
