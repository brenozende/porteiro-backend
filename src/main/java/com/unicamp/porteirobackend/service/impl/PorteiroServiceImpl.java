package com.unicamp.porteirobackend.service.impl;

import com.unicamp.porteirobackend.dto.EmergencyContactDTO;
import com.unicamp.porteirobackend.dto.UserDTO;
import com.unicamp.porteirobackend.dto.request.RegisterForm;
import com.unicamp.porteirobackend.entity.*;
import com.unicamp.porteirobackend.enums.EAuthType;
import com.unicamp.porteirobackend.enums.EUserRole;
import com.unicamp.porteirobackend.repository.ApartmentRepository;
import com.unicamp.porteirobackend.repository.UserRepository;
import com.unicamp.porteirobackend.service.PorteiroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class PorteiroServiceImpl implements PorteiroService {
    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Resident registerResident(RegisterForm form){
        if (form == null)
            return null;
        Resident resident = new Resident();
        resident.setName(form.getName());
        resident.setDocument(form.getDocument());
        resident.setPhoneNumber(form.getPhoneNumber());
        resident.setCreatedAt(new Date());
        resident.setUpdatedAt(new Date());
        resident.setOwner(form.isOwner());

        Set<Role> roles = new HashSet<>();
        roles.add(new Role(EUserRole.RES));

        User user = new User();
        user.setEmail(form.getEmail());
        user.setRoles(roles);
        //TODO: gerar username e senha p/ morador novo
        user.setUsername(form.getEmail());
        user.setPassword("123456");
        resident.setUser(user);

        Set<Visitor> visitors = new HashSet<>();
        form.getVisitors().forEach(v -> {
            Visitor visitor = new Visitor(v, resident);
            visitors.add(visitor);
        });
        resident.setVisitors(visitors);

        Address mailingAddress = new Address(form.getMailingAddress());
        resident.setMailAddress(mailingAddress);

        Apartment apartment = apartmentRepository.findByNumber(form.getApartment());
        if (apartment != null)
            resident.setApartments(Set.of(apartment));

        Set<EmergencyContact> emergencyContacts = new HashSet<>();
        form.getEmergencyContacts().forEach(e -> {
            EmergencyContact emergencyContact = new EmergencyContact(e, resident);
            emergencyContacts.add(emergencyContact);
        });
        resident.setEmergencyContacts(emergencyContacts);

        return resident;
    }

    @Override
    public List<UserDTO> findAllUsers(){
        List<UserDTO> usersDTO = new ArrayList<>();
        List<User> users = userRepository.findAll();
        users.forEach(u -> {
            UserDTO dto = new UserDTO();
            dto.setId(u.getId());
            dto.setUsername(u.getUsername());
            dto.setEmail(u.getEmail());
            usersDTO.add(dto);
        });
        return usersDTO;
    }

    @Override
    public UserDTO getUserById(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty())
            return null;
        UserDTO dto = new UserDTO();
        dto.setId(userOptional.get().getId());
        dto.setUsername(userOptional.get().getUsername());
        dto.setEmail(userOptional.get().getEmail());
        return dto;
    }

    @Override
    public boolean deleteUser(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty())
            return false;
        userRepository.delete(userOptional.get());
        return true;
    }
}
