package com.unicamp.porteirobackend.service;

import com.unicamp.porteirobackend.dto.UserDTO;
import com.unicamp.porteirobackend.entity.User;
import com.unicamp.porteirobackend.enums.UserRole;
import com.unicamp.porteirobackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PorteiroService {
    User createUser(User user);

    UserDTO getUserById(int id);

    List<UserDTO> findAll();

    UserDTO updateUser(int id, UserDTO user);

    boolean deleteUser(int id);
}
