package com.example.Shopzz.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponse {
    private Integer productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Boolean active;
    private Integer categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
