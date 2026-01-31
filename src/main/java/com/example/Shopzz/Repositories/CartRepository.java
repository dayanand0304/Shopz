package com.example.Shopzz.Repositories;

import com.example.Shopzz.Entities.Cart;
import com.example.Shopzz.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
    Optional<Cart> findByUserUserId(Integer userId);
    boolean existsByUserUserId(Integer userId);
}
