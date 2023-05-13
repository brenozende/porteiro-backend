package com.unicamp.porteirobackend.repository;

import com.unicamp.porteirobackend.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResidentRepository extends JpaRepository<Resident, Integer> {
}