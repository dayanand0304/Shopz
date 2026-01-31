package com.example.Shopzz.Repositories;

import com.example.Shopzz.Entities.Order;
import com.example.Shopzz.Entities.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems,Integer> {
    List<OrderItems> findByOrder(Order order);
}
