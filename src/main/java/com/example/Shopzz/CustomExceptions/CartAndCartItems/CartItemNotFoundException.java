package com.example.Shopzz.CustomExceptions.CartAndCartItems;

public class CartItemNotFoundException extends RuntimeException{
    public CartItemNotFoundException(Integer itemId){
        super("Cart item with Id: "+itemId+" not found");
    }
}
