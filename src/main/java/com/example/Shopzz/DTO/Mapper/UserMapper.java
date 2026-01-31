package com.example.Shopzz.DTO.Mapper;

import com.example.Shopzz.DTO.Request.UserCreateRequest;
import com.example.Shopzz.DTO.Request.UserUpdateRequest;
import com.example.Shopzz.DTO.Response.UserResponse;
import com.example.Shopzz.Models.User;

public class UserMapper {
    public static User create(UserCreateRequest request){
        User user=new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        return user;
    }
    public static User update(UserUpdateRequest request){
        User user=new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        return user;
    }
    public static UserResponse response(User user){
        return new UserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
}
