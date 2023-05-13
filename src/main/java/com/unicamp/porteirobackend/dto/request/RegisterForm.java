package com.unicamp.porteirobackend.dto.request;

import com.unicamp.porteirobackend.dto.AddressDTO;
import com.unicamp.porteirobackend.dto.EmergencyContactDTO;
import com.unicamp.porteirobackend.dto.VisitorDTO;
import com.unicamp.porteirobackend.entity.Address;
import com.unicamp.porteirobackend.entity.EmergencyContact;
import com.unicamp.porteirobackend.entity.User;
import com.unicamp.porteirobackend.entity.Visitor;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class RegisterForm {
    private boolean owner;
    private Integer apartment;
    private String realEstate;
    private String name;
    private String document;
    private String phoneNumber;
    @Email
    private String email;
    private AddressDTO mailingAddress;
    private Set<EmergencyContactDTO> emergencyContacts;
    private Set<VisitorDTO> visitors;
    private String filledBy;
}
