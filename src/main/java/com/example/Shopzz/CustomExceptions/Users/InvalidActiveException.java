package com.example.Shopzz.CustomExceptions.Users;

public class InvalidActiveException extends RuntimeException{
    public InvalidActiveException(){
        super("Cannot change active field...use reactiveUser Method");
    }
}
