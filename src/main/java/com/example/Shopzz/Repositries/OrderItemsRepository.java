package com.example.Shopzz.Repositries;

import com.example.Shopzz.Models.Order;
import com.example.Shopzz.Models.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems,Integer> {
    List<OrderItems> findByOrder(Order order);
}
