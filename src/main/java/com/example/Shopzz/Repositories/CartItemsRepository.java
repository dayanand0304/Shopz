package com.example.Shopzz.Repositories;

import com.example.Shopzz.Entities.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems,Integer> {
    Optional<CartItems> findByCart_CartIdAndProduct_ProductId(Integer cartId, Integer productId);
}
