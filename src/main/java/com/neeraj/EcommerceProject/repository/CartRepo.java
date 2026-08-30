package com.neeraj.EcommerceProject.repository;

import com.neeraj.EcommerceProject.model.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<Cart,Integer> {

    @Query(
            "Select c from Cart c where c.userId=:id  "
    )
    List<Cart> findByUserId(int id);

   @Query("Select c from Cart c where c.userId=:uid and c.productId=:pid")
    Optional<Cart> findByUserIdAndProductId(int uid, int pid);

   @Modifying
   @Transactional
   @Query("delete from Cart c where c.userId=:uid and c.productId=:pid ")
    void deleteByProductIdAndUserId(int uid,int pid);
}
