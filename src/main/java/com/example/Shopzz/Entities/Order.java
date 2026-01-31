package com.example.Shopzz.Entities;

import com.example.Shopzz.Enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItems> orderItems=new ArrayList<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal itemsTotal;   // sum of line totals

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal taxAmount;    // GST/VAT

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal deliveryFee;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal grandTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false,updatable = false)
    private LocalDateTime orderDate;

    private LocalDateTime shippedDate;
    private LocalDateTime expectedDeliveryDate;
    private LocalDateTime deliveredDate;
    private LocalDateTime canceledDate;

    @PrePersist
    void onCreate() {
        orderDate = LocalDateTime.now();
        if (status == null) status = OrderStatus.CREATED;
    }

    public void addItem(OrderItems items){
        orderItems.add(items);
        items.setOrder(this);
    }

    public void removeItem(OrderItems items){
        orderItems.remove(items);
        items.setOrder(null);
    }
}
