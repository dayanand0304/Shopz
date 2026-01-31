package com.example.Shopzz.DTO.Mapper;

import com.example.Shopzz.DTO.Response.CartItemsResponse;
import com.example.Shopzz.DTO.Response.CartResponse;
import com.example.Shopzz.Entities.Cart;

import java.util.List;

public class CartMapper {
    public static CartResponse response(Cart cart){
        List<CartItemsResponse> items=
                cart.getItems()
                        .stream()
                        .map(CartItemsMapper::response)
                        .toList();

        return new CartResponse(
                cart.getCartId(),
                cart.getUser().getUserId(),
                items,
                cart.getCartTotal()
        );
    }
}
