package com.example.Shopzz.CustomExceptions.CartAndCartItems;

public class CartItemNotFoundInCartException extends RuntimeException{
    public CartItemNotFoundInCartException(Integer id){
        super("Cart item with id: "+id+" not in the cart");
    }
}
