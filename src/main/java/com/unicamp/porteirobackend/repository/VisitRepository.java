package com.unicamp.porteirobackend.repository;

import com.unicamp.porteirobackend.entity.Visit;
import com.unicamp.porteirobackend.enums.EVisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Integer> {

    List<Visit> findByVisitStatus(EVisitStatus status);
}
