package com.example.Shopzz.Services;

import com.example.Shopzz.Entities.Order;
import com.example.Shopzz.Entities.OrderItems;
import com.example.Shopzz.Repositories.OrderItemsRepository;
import com.example.Shopzz.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemsService {

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private OrderRepository orderRepository;


    //GET ORDER ITEMS FROM ORDER ID
    public List<OrderItems> getItemsByOrderId(Integer orderId){
        Order order=orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order id with "+orderId+" not found"));

        return orderItemsRepository.findByOrder(order);
    }


}
