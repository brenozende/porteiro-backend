package com.unicamp.porteirobackend.repository;

import com.unicamp.porteirobackend.entity.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Integer> {
}