package com.neeraj.EcommerceProject.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;

    private int orderId;

    private int productId;

    private int quantity;

    private double price;
}