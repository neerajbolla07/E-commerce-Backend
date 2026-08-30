package com.neeraj.EcommerceProject.repository;

import com.neeraj.EcommerceProject.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products,Integer> {

    @Query("Select p from Products p where Lower(p.name) like " +
            "Lower(Concat('%',:keyword,'%')) OR Lower(p.description)" +
            " like Lower(Concat('%',:keyword,'%')) or Lower(p.brand)" +
            "like Lower(Concat('%',:keyword,'%')) or lower(p.category)"+
            "like Lower(Concat('%',:keyword,'%'))"
    )
    List<Products> searchProducts(String keyword);
}
