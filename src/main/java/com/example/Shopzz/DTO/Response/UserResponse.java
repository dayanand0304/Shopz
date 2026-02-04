package com.example.Shopzz.DTO.Response;

import com.example.Shopzz.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private Integer userId;
    private String username;
    private String email;
    private Role role;
    private Boolean active;
    private LocalDateTime deletedAt;
}
