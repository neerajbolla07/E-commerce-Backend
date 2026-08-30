package com.neeraj.EcommerceProject.service;

import com.neeraj.EcommerceProject.model.Cart;
import com.neeraj.EcommerceProject.model.Products;
import com.neeraj.EcommerceProject.repository.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    CartRepo repo;
    public List<Cart> getCartProducts(int id) {
       List<Cart> crt= repo.findByUserId(id);
       return  crt;
    }

    public void addProductToCart(int uid,Products prod) {
        Cart crt=new Cart();
        crt.setUserId(uid);
        crt.setProductId(prod.getId());
        crt.setQuantity(1);
        crt.setPrice(prod.getPrice().doubleValue());

        repo.save(crt);
    }

    public void increaseQuantity(int uid, Products product) {
        Cart cart=repo.findByUserIdAndProductId(uid,product.getId()).orElse(null);
        assert cart != null;
        cart.setQuantity(cart.getQuantity()+1);
        repo.save(cart);

    }

    public void decreaseQuantity(int uid, Products product) {
        Cart cart=repo.findByUserIdAndProductId(uid,product.getId()).orElse(null);
        assert cart != null;
        if(cart.getQuantity()>1)
        {
            cart.setQuantity(cart.getQuantity()-1);
            repo.save(cart);
        }
        else if(cart.getQuantity()==1)
        {
            repo.deleteByProductIdAndUserId(uid,product.getId());
        }

    }

    public void removeItem(int pid, int uid) {
        repo.deleteByProductIdAndUserId(pid,uid);
    }
}
