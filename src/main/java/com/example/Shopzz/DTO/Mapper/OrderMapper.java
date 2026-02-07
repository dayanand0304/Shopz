package com.example.Shopzz.DTO.Mapper;

import com.example.Shopzz.DTO.Response.OrderItemResponse;
import com.example.Shopzz.DTO.Response.OrderResponse;
import com.example.Shopzz.Entities.Order;
import java.util.List;

public class OrderMapper {
    public static OrderResponse response(Order order){

        List<OrderItemResponse> orderItems =
                order.getOrderItems()
                        .stream()
                        .map(OrderItemsMapper::response)
                        .toList();
        return new OrderResponse(
                order.getOrderId(),
                order.getUser().getUserId(),
                orderItems,
                order.getItemsTotal(),
                order.getTaxAmount(),
                order.getDeliveryFee(),
                order.getDiscountAmount(),
                order.getGrandTotal(),
                order.getStatus(),
                order.getOrderDate(),
                order.getExpectedDeliveryDate()
        );
    }
}
