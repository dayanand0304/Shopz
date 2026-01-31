package com.example.Shopzz.DTO.Mapper;

import com.example.Shopzz.DTO.Response.OrderItemResponse;
import com.example.Shopzz.Entities.OrderItems;

public class OrderItemsMapper {
    public static OrderItemResponse response(OrderItems items){
        return new OrderItemResponse(
                items.getOrderItemsId(),
                items.getProduct().getProductId(),
                items.getProduct().getProductName(),
                items.getQuantity(),
                items.getPriceAtOrderTime(),
                items.getItemsTotal()
        );
    }
}
