package com.neeraj.EcommerceProject.repository;

import com.neeraj.EcommerceProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query(
            "Select u from User u where u.email = :email"
    )
    User findByEmail(String email);

}
