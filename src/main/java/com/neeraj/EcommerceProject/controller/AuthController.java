package com.neeraj.EcommerceProject.controller;

import com.neeraj.EcommerceProject.model.User;
import com.neeraj.EcommerceProject.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    AuthenticationService as;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User auth)
    {
        as.signup(auth);
        return new ResponseEntity<>("Signup successful",HttpStatus.OK );

    }

    @PostMapping ("/login")
    public ResponseEntity<User> login(@RequestBody User auth)
    {
        User u=as.getUserDetails(auth.getEmail());
        if(u!=null)
        {
            if(u.getPass().equals(auth.getPass()))
            {
                return new ResponseEntity<>(u,HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }


}
