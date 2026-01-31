package com.example.Shopzz.CustomExceptions.CartAndCartItems;

public class QuantityException extends RuntimeException{
    public QuantityException(Integer stock){
        super("Only " + stock + " available");
    }

    public QuantityException(){
        super("Quantity must be positive");
    }
}
