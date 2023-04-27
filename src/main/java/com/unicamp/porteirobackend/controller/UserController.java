package com.unicamp.porteirobackend.controller;

import java.util.List;
import java.util.Optional;

import com.unicamp.porteirobackend.repository.UserRepository;
import com.unicamp.porteirobackend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repository;
    @GetMapping
    public List<User> getAllUsers(){
        return repository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        String rawPassword = user.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(rawPassword));
        return repository.save(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id){
        Optional<User> optionalUser = repository.findById(id);
        return optionalUser.orElse(null);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user){
        Optional<User> optionalUser = repository.findById(id);
        if(optionalUser.isPresent()){
            User existingUser = optionalUser.get();
            existingUser.setName(user.getName());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setUserRole(user.getUserRole());
            return repository.save(existingUser);
        }else{
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id){
        repository.deleteById(id);
    }
}
