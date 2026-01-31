package com.example.Shopzz.CustomExceptions.Orders;

import com.example.Shopzz.Enums.OrderStatus;

public class OrderCannotUpdateException extends RuntimeException{
    public OrderCannotUpdateException(OrderStatus status){
        super("Cannot update status from " + status);
    }
}
