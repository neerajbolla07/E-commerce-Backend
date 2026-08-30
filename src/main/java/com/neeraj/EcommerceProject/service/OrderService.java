package com.neeraj.EcommerceProject.service;

import com.neeraj.EcommerceProject.model.Cart;
import com.neeraj.EcommerceProject.model.Order;
import com.neeraj.EcommerceProject.model.OrderItem;
import com.neeraj.EcommerceProject.repository.OrderItemRepository;
import com.neeraj.EcommerceProject.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    CartService cartService;


    // Create order from user's cart
    public Order createOrder(int uid) {

        List<Cart> cartItems = cartService.getCartProducts(uid);

        // Don't create order if cart is empty
        if (cartItems.isEmpty()) {
            return null;
        }

        // Create Order
        Order order = new Order();

        order.setUserId(uid);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");

        double total = 0;

        // First save order so we get orderId
        Order savedOrder = orderRepository.save(order);


        // Create OrderItems
        for (Cart cart : cartItems) {

            OrderItem item = new OrderItem();

            item.setOrderId(savedOrder.getOrderId());
            item.setProductId(cart.getProductId());
            item.setQuantity(cart.getQuantity());
            item.setPrice(cart.getPrice());

            total += cart.getQuantity() * cart.getPrice();

            orderItemRepository.save(item);
        }


        // Update total
        savedOrder.setTotalAmount(total);

        orderRepository.save(savedOrder);


        // Clear cart
        for (Cart cart : cartItems) {

            cartService.removeItem(
                    cart.getProductId(),
                    uid
            );
        }

        return savedOrder;
    }


    // Get all orders of a user
    public List<Order> getOrders(int uid) {

        return orderRepository.findByUserId(uid);
    }


    // Get one order
    public Order getOrder(int orderId) {

        return orderRepository
                .findById(orderId)
                .orElse(null);
    }


    // Update order status
    public Order updateStatus(
            int orderId,
            String status) {

        Order order = orderRepository
                .findById(orderId)
                .orElse(null);

        if (order == null) {
            return null;
        }

        order.setStatus(status);

        return orderRepository.save(order);
    }
}