package com.unicamp.porteirobackend.repository;

import com.unicamp.porteirobackend.dto.VisitDTO;
import com.unicamp.porteirobackend.entity.Resident;
import com.unicamp.porteirobackend.entity.Visit;
import com.unicamp.porteirobackend.entity.Visitor;
import com.unicamp.porteirobackend.enums.EVisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Integer> {
    List<Visit> findByVisitStatusAndVisitor_Resident(EVisitStatus visitStatus, Resident resident);
    List<Visit> findByVisitStatus(EVisitStatus visitStatus);
    List<Visit> findByVisitStatusAndVisitorIn(EVisitStatus visitStatus, Set<Visitor> visitors);
}
