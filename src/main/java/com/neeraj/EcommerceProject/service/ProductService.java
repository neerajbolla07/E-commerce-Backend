package com.neeraj.EcommerceProject.service;

import com.neeraj.EcommerceProject.model.Products;
import com.neeraj.EcommerceProject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository pr;
    public List<Products> getProducts()
    {
        return pr.findAll();
    }

    public Products getProduct(int id) {
        return pr.findById(id).orElse(null);
    }

    public Products addProduct(Products prod, MultipartFile image) throws IOException {
        prod.setImageName(image.getOriginalFilename());
        prod.setImageType(image.getContentType());
        prod.setImageFile(image.getBytes());
        pr.save(prod);

        return prod;
    }

    public void updateProduct(int id,Products prod,MultipartFile image) throws IOException {
        prod.setImageName(image.getOriginalFilename());
        prod.setImageType(image.getContentType());
        prod.setImageFile(image.getBytes());
        pr.save(prod);
    }

    public void deleteProduct(int id) {
        pr.deleteById(id);
    }

    public List<Products> searchProducts(String keyword) {
        return pr.searchProducts(keyword);
    }
}
