package com.example.Shopzz.CustomExceptions.Users;

public class UserEmailAlreadyExistsException extends RuntimeException{
    public UserEmailAlreadyExistsException(String email){
        super("User with Email: "+email+" already exists");
    }
}
