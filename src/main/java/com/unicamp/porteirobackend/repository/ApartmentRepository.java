package com.unicamp.porteirobackend.repository;

import com.unicamp.porteirobackend.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartmentRepository extends JpaRepository<Apartment, Integer> {
    Apartment findByNumber(Integer number);
}