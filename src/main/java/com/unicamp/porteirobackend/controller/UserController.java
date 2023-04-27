package com.unicamp.porteirobackend.controller;

import java.util.List;

import com.unicamp.porteirobackend.repository.UserRepository;
import com.unicamp.porteirobackend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserRepository repository;
    @GetMapping
    public List<User> list(){
        return repository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        return repository.save(user);
    }
}
