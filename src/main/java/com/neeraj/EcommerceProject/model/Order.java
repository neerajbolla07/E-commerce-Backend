package com.neeraj.EcommerceProject.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    private int userId;

    private double totalAmount;

    private LocalDateTime orderDate;

    private String status;
}