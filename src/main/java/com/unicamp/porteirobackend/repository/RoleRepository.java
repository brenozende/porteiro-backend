package com.unicamp.porteirobackend.repository;

import com.unicamp.porteirobackend.entity.Role;
import com.unicamp.porteirobackend.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(UserRole name);
}