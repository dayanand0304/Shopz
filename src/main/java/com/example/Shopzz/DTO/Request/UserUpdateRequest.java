package com.example.Shopzz.DTO.Request;

import com.example.Shopzz.Enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    @Size(min = 3,max = 50, message = "username must be 3-50 characters")
    private String username;

    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 6,max = 100,message = "password must be 6-100 characters")
    private String password;

    private Role role;

    private Boolean active;
}
