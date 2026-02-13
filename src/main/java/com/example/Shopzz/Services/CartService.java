package com.example.Shopzz.Services;

import com.example.Shopzz.CustomExceptions.CartAndCartItems.CartAlreadyExistsForUserException;
import com.example.Shopzz.CustomExceptions.CartAndCartItems.CartNotFoundException;
import com.example.Shopzz.CustomExceptions.CartAndCartItems.CartNotFoundForUserException;
import com.example.Shopzz.CustomExceptions.Users.UserNotFoundException;
import com.example.Shopzz.DTO.Mapper.CartMapper;
import com.example.Shopzz.DTO.Response.CartResponse;
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
    public CartResponse getCartByUserId(Integer userId){
        Cart cart=cartRepository.findCartWithItemsByUser_UserId(userId)
                .orElseThrow(()->new CartNotFoundForUserException(userId));

        return CartMapper.response(cart);
    }

    //GET CART BY CART ID
    public CartResponse getCartByCartId(Integer cartId){
        Cart cart=cartRepository.findCartWithItemsByCartId(cartId)
                .orElseThrow(()->new CartNotFoundException(cartId));

        return CartMapper.response(cart);
    }

    //CREATE CART BY USER ID
    public CartResponse createCart(Integer userId) {
        if (cartRepository.existsByUserUserId(userId)) {
            throw new CartAlreadyExistsForUserException(userId);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        Cart saved=cartRepository.save(cart);

        return CartMapper.response(saved);
    }
}
