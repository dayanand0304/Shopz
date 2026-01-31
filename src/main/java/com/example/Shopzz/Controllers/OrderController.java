package com.example.Shopzz.Controllers;

import com.example.Shopzz.DTO.Mapper.OrderMapper;
import com.example.Shopzz.DTO.Request.OrderCreateRequest;
import com.example.Shopzz.DTO.Response.OrderResponse;
import com.example.Shopzz.Entities.Order;
import com.example.Shopzz.Enums.OrderStatus;
import com.example.Shopzz.Services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //GET ALL ORDERS
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll(){
        List<OrderResponse> orders=orderService.getAllOrders()
                .stream()
                .map(OrderMapper::response)
                .toList();

        return ResponseEntity.ok(orders);
    }

    //GET ORDERS BY USER ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrderByUserId(@PathVariable Integer userId){
        List<OrderResponse> orders=orderService.getOrdersByUserId(userId)
                .stream()
                .map(OrderMapper::response)
                .toList();
        return ResponseEntity.ok(orders);
    }

    //GET ORDERS BY STATUS
    @GetMapping("/status")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@RequestParam OrderStatus status){
        List<OrderResponse> orders=orderService.getOrdersByStatus(status)
                .stream()
                .map(OrderMapper::response)
                .toList();
        return ResponseEntity.ok(orders);
    }

    //PLACE ORDER
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderCreateRequest request){
        Order order=orderService.placeOrder(request.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OrderMapper.response(order));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Integer orderId){
        Order order=orderService.cancelOrder(orderId);

        return ResponseEntity.ok(OrderMapper.response(order));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Integer orderId,
                                                           @RequestParam OrderStatus status){
        Order order=orderService.updateOrderStatus(orderId,status);
        return ResponseEntity.ok(OrderMapper.response(order));

    }
}
