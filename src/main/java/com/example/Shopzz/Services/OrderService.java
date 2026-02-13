package com.example.Shopzz.Services;

import com.example.Shopzz.CustomExceptions.CartAndCartItems.CartEmptyException;
import com.example.Shopzz.CustomExceptions.CartAndCartItems.CartNotFoundForUserException;
import com.example.Shopzz.CustomExceptions.CartAndCartItems.QuantityException;
import com.example.Shopzz.CustomExceptions.Orders.InvalidStatusException;
import com.example.Shopzz.CustomExceptions.Orders.OrderAlreadyCancelledException;
import com.example.Shopzz.CustomExceptions.Orders.OrderCannotUpdateException;
import com.example.Shopzz.CustomExceptions.Orders.OrderNotFoundException;
import com.example.Shopzz.CustomExceptions.Users.UserNotFoundException;
import com.example.Shopzz.DTO.Mapper.OrderMapper;
import com.example.Shopzz.DTO.PageMapper;
import com.example.Shopzz.DTO.Response.OrderResponse;
import com.example.Shopzz.DTO.Response.PageResponse;
import com.example.Shopzz.Entities.*;
import com.example.Shopzz.Enums.OrderStatus;
import com.example.Shopzz.Repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;


    //GET ALL ORDERS
    public PageResponse<OrderResponse> getAllOrders(Pageable pageable){
        Page<Order> page =orderRepository.findAllWithItems(pageable);

        return PageMapper.toPageResponse(page, OrderMapper::response);
    }

    //GET ORDERS BY USER ID
    public PageResponse<OrderResponse> getOrdersByUserId(Integer userId,Pageable pageable){
        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        Page<Order> page=orderRepository.findOrderByUserIdWithItems(userId,pageable);
        return PageMapper.toPageResponse(page,OrderMapper::response);
    }

    //GET ORDER BY ORDER ID
    public OrderResponse getOrderByOrderId(Integer orderId){
        Order order=orderRepository.findById(orderId)
                .orElseThrow(()->new OrderNotFoundException(orderId));

        return OrderMapper.response(order);
    }

    //GET ORDERS BY STATUS
    public PageResponse<OrderResponse> getOrdersByStatus(OrderStatus status,Pageable pageable) {
        Page<Order> page=orderRepository.findOrderByStatusWithItems(status,pageable);

        return PageMapper.toPageResponse(page,OrderMapper::response);
    }

    //PLACE ORDER
    @Transactional
    public OrderResponse placeOrder(Integer userId){

        User user=userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException(userId));

        Cart cart=cartRepository.findByUserUserId(user.getUserId())
                .orElseThrow(()->new CartNotFoundForUserException(userId));


        if(cart.getItems().isEmpty()){
            throw new CartEmptyException(userId);
        }

        Order order=new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);

        BigDecimal itemsTotal=BigDecimal.ZERO;

        for(CartItems item :cart.getItems()){

            Product product = item.getProduct();

            // Check stock availability
            if (product.getStock() < item.getQuantity()) {
                throw new QuantityException(product.getProductName());
            }

            // Reduce stock
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);

            OrderItems orderItem=new OrderItems();
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPriceAtOrderTime(item.getPriceAtAddTime());
            order.addItem(orderItem);


            itemsTotal=itemsTotal.add(item.getProductTotal());
        }

        calculateAmount(order,itemsTotal);
        order.setExpectedDeliveryDate(LocalDateTime.now().plusDays(5));

        Order savedOrder=orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        Order saved=orderRepository.findOrderWithItems(savedOrder.getOrderId())
                .orElseThrow(()->new OrderNotFoundException(savedOrder.getOrderId()));

        return OrderMapper.response(order);
    }

    //CALCULATE AMOUNT
    private void calculateAmount(Order order,BigDecimal itemsTotal){
        BigDecimal tax=itemsTotal.multiply(new BigDecimal("0.18"));

        BigDecimal delivery=
                itemsTotal.compareTo(new BigDecimal("1000"))>=0
                        ? BigDecimal.ZERO : new BigDecimal("50");

        BigDecimal discount;
        if(itemsTotal.compareTo(new BigDecimal("10000"))>=0){
            discount=new BigDecimal("1000");
        }else if(itemsTotal.compareTo(new BigDecimal("5000"))>=0){
            discount=new BigDecimal("500");
        }else if(itemsTotal.compareTo(new BigDecimal("3000"))>=0){
            discount=new BigDecimal("300");
        }else{
            discount=BigDecimal.ZERO;
        }
        BigDecimal grandTotal= itemsTotal.add(tax).add(delivery).subtract(discount);

        order.setItemsTotal(itemsTotal);
        order.setTaxAmount(tax);
        order.setDeliveryFee(delivery);
        order.setDiscountAmount(discount);
        order.setGrandTotal(grandTotal);
    }

    //CANCEL ORDER
    @Transactional
    public void cancelOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (order.getStatus()==OrderStatus.CANCELLED) {
            throw new OrderAlreadyCancelledException();
        }

        // Restore stock for each product
        for (OrderItems item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
        }

        // MARK ORDER AS CANCELLED
        order.setStatus(OrderStatus.CANCELLED);
        order.setCanceledDate(LocalDateTime.now());

        orderRepository.save(order);
    }

    //UPDATE STATUS
    @Transactional
    public OrderResponse updateOrderStatus(Integer orderId,OrderStatus newStatus) {
        Order order = orderRepository.findOrderWithItems(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if(order.getStatus()==OrderStatus.CANCELLED ||
                order.getStatus()==OrderStatus.DELIVERED){
            throw new OrderCannotUpdateException(newStatus);
        }

        switch(newStatus){
            case SHIPPED -> {
                order.setStatus(OrderStatus.SHIPPED);
                order.setShippedDate(LocalDateTime.now());
            }

            case DELIVERED -> {
                order.setStatus(OrderStatus.DELIVERED);
                order.setDeliveredDate(LocalDateTime.now());
            }
            case CANCELLED -> {
                cancelOrder(orderId);
                Order saved=orderRepository.findOrderWithItems(orderId)
                        .orElseThrow(()->new OrderNotFoundException(orderId));

                return OrderMapper.response(saved);
            }
            default -> throw new InvalidStatusException();
        }
        Order saved=orderRepository.save(order);
        return OrderMapper.response(saved);
    }
}
