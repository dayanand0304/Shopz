package com.example.Shopzz.Repositries;

import com.example.Shopzz.Models.Cart;
import com.example.Shopzz.Models.CartItems;
import com.example.Shopzz.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems,Integer> {
    List<CartItems> findByCart(Cart cart);
    Optional<CartItems> findByCartAndProduct(Cart cart, Product product);
}
