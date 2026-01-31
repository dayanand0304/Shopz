package com.example.Shopzz.CustomExceptions.CartAndCartItems;

public class CartEmptyException extends RuntimeException{
    public CartEmptyException(Integer userId){
        super("Cart with user Id: "+userId+" is Empty");
    }
}
