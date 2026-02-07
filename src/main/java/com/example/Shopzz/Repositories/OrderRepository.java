package com.example.Shopzz.Repositories;

import com.example.Shopzz.Entities.Order;
import com.example.Shopzz.Enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByUserUserId(Integer userId);
    List<Order> findByStatus(OrderStatus status);

    //GET ALL ORDERS
    @Query("""
            SELECT DISTINCT o FROM Order as o
            LEFT JOIN FETCH o.orderItems as items
            LEFT JOIN FETCH items.product
            ORDER BY o.orderDate DESC
            """)
    List<Order> findAllWithItems();

    //GET ORDER BY ORDER ID
    @Query("""
            SELECT o FROM Order as o
            LEFT JOIN FETCH o.orderItems as items
            LEFT JOIN FETCH items.product
            WHERE o.orderId=:orderId
            """)
    Optional<Order> findOrderWithItems(@Param("orderId") Integer orderId);

    //GET ALL ORDER OF USER WITH USER ID
    @Query("""
            SELECT DISTINCT o FROM Order as o
            LEFT JOIN FETCH o.orderItems as items
            LEFT JOIN FETCH items.product
            WHERE o.user.userId=:userId
            ORDER BY o.orderDate DESC
            """)
    List<Order> findOrderByUserIdWithItems(@Param("userId") Integer userId);

    @Query("""
            SELECT o FROM Order as o
            LEFT JOIN FETCH o.orderItems as items
            LEFT JOIN FETCH items.product
            WHERE o.status=:status
            ORDER BY o.orderDate DESC
            """)
    List<Order> findOrderByStatusWithItems(@Param("status") OrderStatus status);
}
