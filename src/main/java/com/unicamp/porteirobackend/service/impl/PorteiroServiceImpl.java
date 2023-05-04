package com.unicamp.porteirobackend.service.impl;

import com.unicamp.porteirobackend.dto.UserDTO;
import com.unicamp.porteirobackend.entity.User;
import com.unicamp.porteirobackend.enums.UserRole;
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
    public User createUser(User user) {
        try {
            validateUserData(user);
            String rawPassword = user.getPassword();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(rawPassword));
            userRepository.save(user);
            return user;
        } catch (UserCreationException e) {
            log.error(e.getErrorMsg());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void validateUserData(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null)
            throw new UserCreationException("User email already in use");
        existingUser = userRepository.findByPhoneNumber(user.getPhoneNumber());
        if (existingUser != null)
            throw new UserCreationException("User phone number already in use");
    }

    @Override
    public UserDTO getUserById(int id) {
        Optional<User> userOp = userRepository.findById(id);
        return userOp.map(UserDTO::new).orElse(null);
    }

    @Override
    public List<UserDTO> findAll(){
        List<User> users = userRepository.findAll();
        List<UserDTO> list = new ArrayList<>();
        if (!users.isEmpty())
            users.forEach(u -> list.add(new UserDTO(u)));
        return list;
    }

    @Override
    public UserDTO updateUser(int id, UserDTO updatedUser) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty())
            return null;
        User user = userOptional.get();
        if (updatedUser != null) {
            if (updatedUser.getName() != null)
                user.setName(updatedUser.getName());
            if (updatedUser.getEmail() != null)
                user.setEmail(updatedUser.getEmail());
            if (updatedUser.getPhoneNumber() != null)
                user.setPhoneNumber(updatedUser.getPhoneNumber());
            if (updatedUser.getVisitors() != null && !updatedUser.getVisitors().isEmpty())
                user.setVisitors(updatedUser.getVisitors());
            userRepository.save(user);
        }
        return new UserDTO(user);
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
