package com.example.Shopzz.Controllers;

import com.example.Shopzz.DTO.Request.OrderCreateRequest;
import com.example.Shopzz.DTO.Response.OrderResponse;
import com.example.Shopzz.DTO.Response.PageResponse;
import com.example.Shopzz.Enums.OrderStatus;
import com.example.Shopzz.Services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //GET ALL ORDERS
    @GetMapping
    public ResponseEntity<PageResponse<OrderResponse>> getAll(@PageableDefault(size = 5)Pageable pageable){

        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    //GET ORDERS BY USER ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<PageResponse<OrderResponse>> getOrderByUserId(@PathVariable Integer userId,
                                                                        @PageableDefault(size = 5)Pageable pageable){
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId,pageable));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderResponse> getByOrderId(@PathVariable Integer orderId){

        return ResponseEntity.ok(orderService.getOrderByOrderId(orderId));
    }

    //GET ORDERS BY STATUS
    @GetMapping("/status")
    public ResponseEntity<PageResponse<OrderResponse>> getOrdersByStatus(@RequestParam OrderStatus status,
                                                                         @PageableDefault(size = 5)Pageable pageable){
        return ResponseEntity.ok(orderService.getOrdersByStatus(status,pageable));
    }

    //PLACE ORDER
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.placeOrder(request.getUserId()));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Integer orderId){
        orderService.cancelOrder(orderId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Integer orderId,
                                                           @RequestParam OrderStatus status){
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId,status));
    }
}
