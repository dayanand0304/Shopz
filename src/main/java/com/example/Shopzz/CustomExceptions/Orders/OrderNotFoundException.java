package com.example.Shopzz.CustomExceptions.Orders;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(Integer orderId){
        super("Order with Id: "+orderId+" not found");
    }
}
