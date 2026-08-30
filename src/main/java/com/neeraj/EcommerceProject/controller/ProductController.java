package com.neeraj.EcommerceProject.controller;

import com.neeraj.EcommerceProject.model.Products;
import com.neeraj.EcommerceProject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService prs;

    @RequestMapping("/")
    public String home()
    {
        return "Welcome to my ecommerce website";
    }

    @GetMapping("/products")
    public ResponseEntity<List<Products>>getProducts()
    {

        return new ResponseEntity<>(prs.getProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Products> getProduct(@PathVariable int id)
    {
        Products prod= prs.getProduct(id);
        if(prod!=null)
        {
            return new ResponseEntity<>(prod,HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(prod,HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart("prod") Products prod,
                                         @RequestPart("image") MultipartFile image)
    {

        try
        {
            Products prd=prs.addProduct(prod,image);
            return new ResponseEntity<>(prd,HttpStatus.CREATED);
        }
        catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{id}/image")
    public ResponseEntity<byte[]> getImageById(@PathVariable int id)
    {
        Products prod=prs.getProduct(id);
        byte[] img= prod.getImageFile();
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(prod.getImageType()))
                .body(img);
    }
    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@RequestPart("prod") Products prod,
                                                  @RequestPart("image") MultipartFile image,
                                                  @PathVariable int id)

    {
        Products pds=prs.getProduct(id);
            if(pds!=null){
                try {
                    prs.updateProduct(id,prod,image);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return new ResponseEntity<>("Product updated",HttpStatus.OK);}
            else {
                return new ResponseEntity<>("Product Not Found",HttpStatus.NOT_FOUND);
            }
    }

    @DeleteMapping("product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id)
    {
        Products pds=prs.getProduct(id);
        if(pds!=null)
        {
            prs.deleteProduct(id);
            return new ResponseEntity<>("Deleted the product",HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Could not delete the product",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("products/search")
    public ResponseEntity<List<Products>> searchProducts(@RequestParam String keyword)
    {
       List<Products> prods= prs.searchProducts(keyword);
        return new ResponseEntity<>(prods,HttpStatus.OK);
    }

}
