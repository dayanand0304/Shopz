package com.example.Shopzz.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderItemsId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id",nullable = false)
    @JsonBackReference
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal priceAtOrderTime;

    //Line Total (price*quantity)
    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal itemsTotal;

    @PrePersist
    void calculateLineTotal(){
        if(priceAtOrderTime != null && quantity != null){
            itemsTotal = priceAtOrderTime.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
