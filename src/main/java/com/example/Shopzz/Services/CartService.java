package com.example.Shopzz.Services;

import com.example.Shopzz.Models.Cart;
import com.example.Shopzz.Models.User;
import com.example.Shopzz.Repositries.CartRepository;
import com.example.Shopzz.Repositries.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    private final Logger log= LoggerFactory.getLogger(CartService.class);


    //GET ALL CARTS OF ALL USERS
    public List<Cart> getAllCarts(){
        return cartRepository.findAll();
    }


    //GET CARTS BY USER ID
    public Cart getCartsByUserId(Integer userId){
        User user=userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User with id " + userId + " not found"));
        return cartRepository.findByUser(user);
    }

    //GET CART BY CART ID
    public Optional<Cart> getCartByCartId(Integer cartId){
        return cartRepository.findById(cartId);
    }

    //CREATE CART BY USER ID
    public Cart createCart(Integer userId){
        User user=userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User with id " + userId + " not found"));

        Cart cart=new Cart();
        cart.setUser(user);
        Cart saved=cartRepository.save(cart);
        log.info("Created cart id={} for userId={}", saved.getCartId(), userId);
        return saved;
    }

    //DELETE CART BY CART ID
    @Transactional
    public void deleteCart(Integer cartId) {
        if (!cartRepository.existsById(cartId)) {
            log.warn("Attempted to delete non-existing cart id={}", cartId);
            throw new ResourceNotFoundException("Cart with id " + cartId + " not found");
        }
        cartRepository.deleteById(cartId);
        log.info("Deleted cart id={}", cartId);
    }
}
