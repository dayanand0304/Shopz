package com.example.Shopzz.Repositries;

import com.example.Shopzz.Models.Cart;
import com.example.Shopzz.Models.CartItems;
import com.example.Shopzz.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
    Cart findByUser(User user);
}
