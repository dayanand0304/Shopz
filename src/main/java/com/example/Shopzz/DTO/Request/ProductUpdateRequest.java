package com.example.Shopzz.DTO.Request;

import com.example.Shopzz.Models.Category;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductUpdateRequest {

    private String productName;

    @Size(max = 500,message = "description max length is 500")
    private String description;

    @DecimalMin(value="0.0",inclusive = false, message = "price must be positive")
    private BigDecimal price;

    @Min(value = 0, message = "stock must be in positive")
    private Integer stock;

    private Boolean active;

    private Integer categoryId;
}
