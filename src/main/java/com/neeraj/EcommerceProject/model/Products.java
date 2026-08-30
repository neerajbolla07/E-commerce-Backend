package com.neeraj.EcommerceProject.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Products {
    @Id
    private int id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private String category;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern="dd-MM-yyyy")
    private Date releaseDate;
    private boolean available;
    private int quantity;

    private String imageName;
    private String imageType;
    @Lob//Large data. This annotaion actually tells jpa to store it large  obj col
    private byte[] imageFile;
}
