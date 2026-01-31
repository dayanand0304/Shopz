package com.example.Shopzz.Services;

import com.example.Shopzz.Entities.*;
import com.example.Shopzz.Repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    private final Logger log= LoggerFactory.getLogger(OrderService.class);


    //GET ALL ORDERS
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    //GET ORDERS BY USER ID
    public List<Order> getOrdersByUserId(Integer userId){
        User user=userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User Id with"+userId+" not found"));
        return orderRepository.findByUser(user);
    }

    //GET ORDERS BY STATUS
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.getOrdersByStatus(status);
    }

    //PLACE ORDER
    @Transactional
    public Order placeOrder(Integer userId){
        User user=userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User Id with"+userId+" not found"));

        Cart cart=cartRepository.findByUser(user);
        if(cart==null || cart.getItems().isEmpty()){
            throw new ResourceNotFoundException("Cart with " +userId+ "is empty");
        }

        Order order=new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setTotal(cart.getSubTotalPrice());

        List<OrderItems> orderItemsList=new ArrayList<>();

        for(CartItems item :cart.getItems()){

            Product product = item.getProduct();

            // Check stock availability
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getProductName());
            }

            // Reduce stock
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);

            OrderItems orderItem=new OrderItems();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setSubTotal(item.getQuantity()*item.getProduct().getPrice());
            orderItemsList.add(orderItem);
        }

        order.setOrderItems(orderItemsList);

        Order savedOrder=orderRepository.save(order);

        cartItemsRepository.deleteAll(cart.getItems());
        cart.setSubTotalPrice(0);
        cartRepository.save(cart);

        return savedOrder;
    }

    //CANCEL ORDER
    @Transactional
    public String cancelOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order Id with " + orderId+" not found"));

        if ("CANCELED".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Order already canceled!");
        }

        // Get all items for this order
        List<OrderItems> orderItems = orderItemsRepository.findByOrder(order);

        // Restore stock for each product
        for (OrderItems item : orderItems) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        // MARK ORDER AS CANCELLED
        order.setStatus("CANCELED");
        orderRepository.save(order);

        return "Order Id with "+orderId+" has been canceled and stock restored.";
    }

    //UPDATE STATUS
    @Transactional
    public Order updateOrderStatus(Integer orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        String currentStatus = order.getStatus();

        if (currentStatus.equalsIgnoreCase("CANCELED") || currentStatus.equalsIgnoreCase("DELIVERED")) {
            throw new RuntimeException("Cannot update status. Order is already " + currentStatus);
        }

        switch (newStatus.toUpperCase()) {
            case "SHIPPED":
                order.setStatus("SHIPPED");
                order.setShippedDate(LocalDate.now());
                break;

            case "DELIVERED":
                order.setStatus("DELIVERED");
                order.setDeliveredDate(LocalDate.now());
                break;

            case "CANCELED":
                // Restore stock
                List<OrderItems> orderItems = orderItemsRepository.findByOrder(order);
                for (OrderItems item : orderItems) {
                    Product product = item.getProduct();
                    product.setStock(product.getStock() + item.getQuantity());
                    productRepository.save(product);
                }
                order.setStatus("CANCELED");
                order.setCanceledDate(LocalDate.now());
                break;

            default:
                throw new RuntimeException("Invalid status! Allowed: SHIPPED, DELIVERED, CANCELED");
        }

        return orderRepository.save(order);
    }

}
