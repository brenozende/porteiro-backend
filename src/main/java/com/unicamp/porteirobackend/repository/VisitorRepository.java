package com.unicamp.porteirobackend.repository;

import com.unicamp.porteirobackend.entity.Resident;
import com.unicamp.porteirobackend.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Integer> {
    List<Visitor> findByResident(Resident resident);
}
