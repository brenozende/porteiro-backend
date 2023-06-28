package com.unicamp.porteirobackend.repository;

import com.unicamp.porteirobackend.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}