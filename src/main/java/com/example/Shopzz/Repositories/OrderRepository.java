package com.example.Shopzz.Repositories;

import com.example.Shopzz.Entities.Order;
import com.example.Shopzz.Entities.User;
import com.example.Shopzz.Enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByUser(User user);
    List<Order> findByUserUserId(Integer userId);
    List<Order> findByStatus(OrderStatus status);
}
