package com.example.Shopzz.Repositories;

import com.example.Shopzz.Entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
    Optional<Cart> findByUserUserId(Integer userId);
    boolean existsByUserUserId(Integer userId);
    @Query("""
            SELECT c FROM Cart as c
            LEFT JOIN FETCH c.items as i
            LEFT JOIN FETCH i.product
            WHERE c.cartId= :cartId
            """)
    Optional<Cart> findCartWithItemsByCartId(@Param("cartId")Integer cartId);

    @Query("""
            SELECT DISTINCT c FROM Cart as c
            LEFT JOIN FETCH c.items as i
            LEFT JOIN FETCH i.product
            WHERE c.user.userId= :userId
            """)
    Optional<Cart> findCartWithItemsByUser_UserId(@Param("userId")Integer userId);
}
