package com.unicamp.porteirobackend.repository;

import com.unicamp.porteirobackend.entity.Visit;
import com.unicamp.porteirobackend.enums.EVisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Integer> {

    List<Visit> findByVisitStatus(EVisitStatus status);
}
