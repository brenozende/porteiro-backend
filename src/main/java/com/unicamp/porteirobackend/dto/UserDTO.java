package com.unicamp.porteirobackend.dto;

import com.unicamp.porteirobackend.entity.User;
import com.unicamp.porteirobackend.entity.Visitor;
import com.unicamp.porteirobackend.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class UserDTO {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private UserRole userRole;
    private Set<Visitor> visitors;

    public UserDTO(User user) {
        this.id = user.getId();
        this.userRole = user.getUserRole();
        this.email = user.getEmail();
        this.name = user.getName();
        this.phoneNumber = user.getPhoneNumber();
    }
}
