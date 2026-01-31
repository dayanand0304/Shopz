package com.example.Shopzz.CustomExceptions.Orders;

public class InvalidStatusException extends RuntimeException{
    public InvalidStatusException(){
        super("Invalid status.");
    }
}
