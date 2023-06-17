package com.unicamp.porteirobackend.repository;

import com.unicamp.porteirobackend.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Integer> {
    Resident findByUser_Id(Integer id);
}