package com.unicamp.porteirobackend.repository;

import com.unicamp.porteirobackend.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorRepository extends JpaRepository<Visitor, Integer> {
}
