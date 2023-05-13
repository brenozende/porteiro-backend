package com.unicamp.porteirobackend.repository;

import com.unicamp.porteirobackend.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
}
