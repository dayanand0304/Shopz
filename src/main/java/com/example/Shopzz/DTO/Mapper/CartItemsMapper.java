package com.example.Shopzz.DTO.Mapper;
import com.example.Shopzz.DTO.Response.CartItemsResponse;
import com.example.Shopzz.Entities.CartItems;

public class CartItemsMapper {
    public static CartItemsResponse response(CartItems items){
        return new CartItemsResponse(
                items.getCartItemId(),
                items.getCart().getCartId(),
                items.getProduct().getProductId(),
                items.getProduct().getProductName(),
                items.getQuantity(),
                items.getPriceAtAddTime(),
                items.getProductTotal()
        );
    }
}
