package com.example.Shopzz.Repositries;

import com.example.Shopzz.Models.Order;
import com.example.Shopzz.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByUser(User user);
    List<Order> getOrdersByStatus(String status);
}
