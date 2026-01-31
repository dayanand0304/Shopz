package com.example.Shopzz.DTO.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateRequest {
    @NotNull(message = "User Id must not be null")
    private Integer userId;
}
