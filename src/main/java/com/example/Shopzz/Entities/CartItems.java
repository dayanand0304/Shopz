package com.example.Shopzz.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemId;

    @ManyToOne
    @JoinColumn(name="cart_id",nullable = false)
    @JsonBackReference
    private Cart cart;

    @ManyToOne
    @JoinColumn(name="product_id",nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtAddTime;

    @Transient
    public BigDecimal getProductTotal(){
        if(priceAtAddTime==null || product.getPrice()==null || quantity==0){
            return BigDecimal.ZERO;
        }
        return priceAtAddTime.multiply(BigDecimal.valueOf(quantity));
    }
}
