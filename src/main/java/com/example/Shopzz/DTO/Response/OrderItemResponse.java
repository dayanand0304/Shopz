package com.example.Shopzz.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class OrderItemResponse {
    private Integer orderItemId;
    private Integer productId;
    private String productName;
    private Integer quantity;
    private BigDecimal priceAtOrderTime;
    private BigDecimal itemsTotal;
}
