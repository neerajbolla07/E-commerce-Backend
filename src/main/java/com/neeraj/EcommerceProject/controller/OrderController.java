package com.neeraj.EcommerceProject.controller;

import com.neeraj.EcommerceProject.model.Order;
import com.neeraj.EcommerceProject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;


    // CHECKOUT
    @PostMapping("/order/checkout")
    public ResponseEntity<Order> checkout(
            @RequestParam int uid) {

        Order order = orderService.createOrder(uid);

        if (order == null) {

            return new ResponseEntity<>(
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
                order,
                HttpStatus.CREATED
        );
    }


    // GET USER ORDERS
    @GetMapping("/orders/{uid}")
    public ResponseEntity<List<Order>> getOrders(
            @PathVariable int uid) {

        return new ResponseEntity<>(
                orderService.getOrders(uid),
                HttpStatus.OK
        );
    }


    // GET SINGLE ORDER
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Order> getOrder(
            @PathVariable int orderId) {

        Order order = orderService.getOrder(orderId);

        if (order == null) {

            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(
                order,
                HttpStatus.OK
        );
    }


    // UPDATE ORDER STATUS
    @PutMapping("/order/{orderId}/status")
    public ResponseEntity<Order> updateStatus(
            @PathVariable int orderId,
            @RequestParam String status) {

        Order order =
                orderService.updateStatus(
                        orderId,
                        status
                );

        if (order == null) {

            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(
                order,
                HttpStatus.OK
        );
    }
}