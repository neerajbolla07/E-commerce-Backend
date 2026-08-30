package com.neeraj.EcommerceProject.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor

public class User {

    @Id
    private int uid;
    private String uname;
    private String name;
    private String email;
    private String pass;
    private String phno;

}
