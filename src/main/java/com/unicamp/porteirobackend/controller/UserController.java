package com.unicamp.porteirobackend.controller;

import java.util.List;
import java.util.Optional;

import com.unicamp.porteirobackend.dto.UserDTO;
import com.unicamp.porteirobackend.enums.UserRole;
import com.unicamp.porteirobackend.repository.UserRepository;
import com.unicamp.porteirobackend.entity.User;
import com.unicamp.porteirobackend.service.PorteiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private PorteiroService porteiroService;
    @GetMapping
    public List<UserDTO> getAllUsers(){
        return porteiroService.findAll();
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user){
        user = porteiroService.createUser(user);
        if (user == null)
            return ResponseEntity.internalServerError().build();

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(new UserDTO(user)).toUri()).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id){
        UserDTO user = porteiroService.getUserById(id);
        if (user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable int id, @RequestBody UserDTO user){
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        UserDTO userDTO = porteiroService.updateUser(id, user);
        if (userDTO == null)
            return ResponseEntity.internalServerError().build();
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id){
        boolean deleted = porteiroService.deleteUser(id);
        if (deleted)
            return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
