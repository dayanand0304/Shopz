package com.example.Shopzz.CustomExceptions.CartAndCartItems;

public class CartAlreadyExistsForUserException extends RuntimeException{
    public CartAlreadyExistsForUserException(Integer userId){
        super("Cart Already Exists for user Id: "+userId);
    }
}
