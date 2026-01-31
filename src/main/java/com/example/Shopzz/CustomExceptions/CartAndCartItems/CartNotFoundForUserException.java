package com.example.Shopzz.CustomExceptions.CartAndCartItems;

public class CartNotFoundForUserException extends RuntimeException{
    public CartNotFoundForUserException(Integer userId){
        super("Cart not found for user id: "+userId);
    }
}
