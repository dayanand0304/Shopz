package com.example.Shopzz.CustomExceptions.Users;

import com.example.Shopzz.Enums.Role;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Integer userId){
        super("User With Id: "+userId+" Not Found");
    }

    public UserNotFoundException(String userName){
        super("User with : "+userName+" Not Found");
    }

    public UserNotFoundException(Role role){
        super("User with Role : "+role+" Not Found");
    }
}
