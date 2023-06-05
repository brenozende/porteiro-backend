package com.unicamp.porteirobackend.repository;

import com.unicamp.porteirobackend.entity.Communications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunicationsRepository extends JpaRepository<Communications, Integer> {
}
