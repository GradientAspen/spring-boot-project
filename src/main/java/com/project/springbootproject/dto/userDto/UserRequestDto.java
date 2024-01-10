package com.project.springbootproject.dto.userDto;

import com.project.springbootproject.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserRequestDto {
    @NotNull
    @Email
    private String email;
    @FieldMatch
    @NotNull
    @Length(min = 8, max = 20)
    private String password;
    @NotNull
    @Length(min = 8, max = 20)
    private String repeatPassword;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String shippingAddress;
}
