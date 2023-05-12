package com.unicamp.porteirobackend.controller;

import java.util.List;

import com.unicamp.porteirobackend.dto.UserDTO;
import com.unicamp.porteirobackend.entity.User;
import com.unicamp.porteirobackend.enums.UserRole;
import com.unicamp.porteirobackend.service.PorteiroService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @GetMapping("/all")
    public String allAccess(){
        return "Public content";
    }

    @GetMapping("/user")
    public String userAccess(){
        return "User content";
    }

    @Autowired
    private PorteiroService porteiroService;

    @GetMapping
    public List<UserDTO> getAllUsers(){
        return porteiroService.findAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id){
        UserDTO user = porteiroService.getUserById(id);
        if (user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id){
        boolean deleted = porteiroService.deleteUser(id);
        if (deleted)
            return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
