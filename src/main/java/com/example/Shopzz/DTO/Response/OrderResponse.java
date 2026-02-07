package com.example.Shopzz.DTO.Response;

import com.example.Shopzz.Enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
    private Integer orderId;
    private Integer userId;
    private List<OrderItemResponse> orderItems;
    private BigDecimal itemsTotal;
    private BigDecimal taxAmount;
    private BigDecimal deliveryFee;
    private BigDecimal discountAmount;
    private BigDecimal grandTotal;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private LocalDateTime expectedDeliveryDate;
}
