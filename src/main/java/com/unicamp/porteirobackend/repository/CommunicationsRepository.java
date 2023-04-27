package com.unicamp.porteirobackend.repository;

import com.unicamp.porteirobackend.entity.Communications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunicationsRepository extends JpaRepository<Communications, Integer> {
}
