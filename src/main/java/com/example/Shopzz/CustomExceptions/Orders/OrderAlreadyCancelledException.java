package com.example.Shopzz.CustomExceptions.Orders;

public class OrderAlreadyCancelledException extends RuntimeException{
    public OrderAlreadyCancelledException(){
        super("Order is Already Cancelled");
    }
}
