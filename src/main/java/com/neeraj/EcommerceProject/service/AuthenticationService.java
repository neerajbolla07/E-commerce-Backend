package com.neeraj.EcommerceProject.service;

import com.neeraj.EcommerceProject.model.User;
import com.neeraj.EcommerceProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    UserRepository repo;
    public void signup(User auth)
    {
        repo.save(auth);
    }

    public User getUserDetails(String email) {
        return repo.findByEmail(email);

    }
}
