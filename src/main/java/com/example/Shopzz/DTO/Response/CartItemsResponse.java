package com.example.Shopzz.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class CartItemsResponse {
    private Integer cartItemId;
    private Integer cartId;
    private Integer productId;
    private String productName;
    private Integer quantity;
    private BigDecimal priceAtAddTime;
    private BigDecimal productTotal;
}
