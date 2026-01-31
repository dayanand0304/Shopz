package com.example.Shopzz.CustomExceptions.CartAndCartItems;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(Integer cartId){
        super("Cart with Id: "+cartId+" not found");
    }
}
