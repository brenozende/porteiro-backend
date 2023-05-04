package com.unicamp.porteirobackend.repository;

import com.unicamp.porteirobackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);
}
