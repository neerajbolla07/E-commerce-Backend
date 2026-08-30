package com.neeraj.EcommerceProject.controller;

import com.neeraj.EcommerceProject.model.Cart;
import com.neeraj.EcommerceProject.model.Products;
import com.neeraj.EcommerceProject.repository.ProductRepository;
import com.neeraj.EcommerceProject.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    CartService crt;

    @Autowired
    ProductController pr;

    @GetMapping ("/cart/{userid}")//To retrieve cart
    public ResponseEntity<List<Cart>> getCartProducts(@PathVariable int userid)
    {
        return new ResponseEntity<>(crt.getCartProducts(userid), HttpStatus.OK);
    }

    @PostMapping("/cart/add")//Add product to cart
    public ResponseEntity<String> addProductToCart(@RequestParam int pid,
                                                   @RequestParam int uid)
    {
        Products product = pr.getProduct(pid).getBody();
        if (product != null) {
            if(product.isAvailable())
            {
                boolean f=false;
                for(Cart c :crt.getCartProducts(uid))
                {
                    if(c.getProductId()==pid)
                    {
                        f=true;
                        increaseQuantity(pid,uid);
                        break;
                    }
                }
                if(f==false){
                    crt.addProductToCart(uid, product);
                }

            }

        }
        return new ResponseEntity<>("Added to cart Successfully",HttpStatus.OK);
    }

    @PutMapping("/cart/incQuantity")
    public ResponseEntity<Void> increaseQuantity(@RequestParam int pid,
                                             @RequestParam int uid)
    {
        Products product = pr.getProduct(pid).getBody();
        crt.increaseQuantity(uid,product);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/cart/DecQuantity")
    public ResponseEntity<Void> decreaseQuantity(@RequestParam int pid,
                                                 @RequestParam int uid)
    {
        Products product = pr.getProduct(pid).getBody();
        crt.decreaseQuantity(uid,product);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/cart/remove")
    public ResponseEntity<String> removeItem(@RequestParam int pid,
                                                 @RequestParam int uid)
    {

        crt.removeItem(pid,uid);
        return new ResponseEntity<>("Product deleted",HttpStatus.OK);
    }




}
