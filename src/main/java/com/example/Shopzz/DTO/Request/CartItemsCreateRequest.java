package com.example.Shopzz.DTO.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemsCreateRequest {

    @NotNull(message = "Cart Id must not be null")
    private Integer cartId;

    @NotNull(message = "Product Id must not be null")
    private Integer productId;

    @NotNull(message = "quantity must not be null")
    @Min(value = 1,message = "Minimum quantity should be 1")
    private Integer quantity;
}
