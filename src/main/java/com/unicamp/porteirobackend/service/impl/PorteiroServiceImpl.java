package com.unicamp.porteirobackend.service.impl;

import com.unicamp.porteirobackend.dto.UserDTO;
import com.unicamp.porteirobackend.entity.User;
import com.unicamp.porteirobackend.exception.UserCreationException;
import com.unicamp.porteirobackend.repository.UserRepository;
import com.unicamp.porteirobackend.service.PorteiroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PorteiroServiceImpl implements PorteiroService {

    @Autowired
    private UserRepository userRepository;

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
