package com.example.Shopzz.Services;

import com.example.Shopzz.CustomExceptions.CartAndCartItems.CartAlreadyExistsForUserException;
import com.example.Shopzz.CustomExceptions.CartAndCartItems.CartNotFoundException;
import com.example.Shopzz.CustomExceptions.CartAndCartItems.CartNotFoundForUserException;
import com.example.Shopzz.CustomExceptions.Users.UserNotFoundException;
import com.example.Shopzz.Entities.Cart;
import com.example.Shopzz.Entities.User;
import com.example.Shopzz.Repositories.CartRepository;
import com.example.Shopzz.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;


    //GET CARTS BY USER ID
    public Cart getCartByUserId(Integer userId){
        return cartRepository.findCartWithItemsByUser_UserId(userId)
                .orElseThrow(()->new CartNotFoundForUserException(userId));
    }

    //GET CART BY CART ID
    public Cart getCartByCartId(Integer cartId){
        return cartRepository.findCartWithItemsByCartId(cartId)
                .orElseThrow(()->new CartNotFoundException(cartId));
    }

    //CREATE CART BY USER ID
    public Cart createCart(Integer userId) {
        if (cartRepository.existsByUserUserId(userId)) {
            throw new CartAlreadyExistsForUserException(userId);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        return cartRepository.save(cart);
    }
}
