package com.example.Shopzz.DTO.Response;

import com.example.Shopzz.Entities.CartItems;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CartResponse {
    private Integer cartId;
    private Integer userId;
    private List<CartItemsResponse> items;
    private BigDecimal cartTotal;
}
