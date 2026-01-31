package com.example.Shopzz.Services;

import com.example.Shopzz.CustomExceptions.CartAndCartItems.CartEmptyException;
import com.example.Shopzz.CustomExceptions.CartAndCartItems.CartNotFoundForUserException;
import com.example.Shopzz.CustomExceptions.CartAndCartItems.QuantityException;
import com.example.Shopzz.CustomExceptions.Orders.InvalidStatusException;
import com.example.Shopzz.CustomExceptions.Orders.OrderAlreadyCancelledException;
import com.example.Shopzz.CustomExceptions.Orders.OrderCannotUpdateException;
import com.example.Shopzz.CustomExceptions.Orders.OrderNotFoundException;
import com.example.Shopzz.CustomExceptions.Users.UserNotFoundException;
import com.example.Shopzz.Entities.*;
import com.example.Shopzz.Enums.OrderStatus;
import com.example.Shopzz.Repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;


    //GET ALL ORDERS
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    //GET ORDERS BY USER ID
    public List<Order> getOrdersByUserId(Integer userId){
        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        List<Order> orders=orderRepository.findByUserUserId(userId);
        return orders;
    }

    //GET ORDERS BY STATUS
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    //PLACE ORDER
    @Transactional
    public Order placeOrder(Integer userId){

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
        order.setExpectedDeliveryDate(LocalDateTime.now().plusDays(5));

        Order savedOrder=orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }

    //CANCEL ORDER
    @Transactional
    public Order cancelOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (order.getStatus()==OrderStatus.CANCELLED) {
            throw new OrderAlreadyCancelledException();
        }

        // Restore stock for each product
        for (OrderItems item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        // MARK ORDER AS CANCELLED
        order.setStatus(OrderStatus.CANCELLED);
        order.setCanceledDate(LocalDateTime.now());

        return orderRepository.save(order);
    }

    //UPDATE STATUS
    @Transactional
    public Order updateOrderStatus(Integer orderId,OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
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
                return cancelOrder(orderId);
            }
            default -> throw new InvalidStatusException();
        }
        return orderRepository.save(order);
    }

}
