package com.example.Shopzz.DTO.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartCreateRequest {
    @NotNull(message = "User id must not be null")
    private Integer userId;
}
