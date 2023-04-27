package com.unicamp.porteirobackend.repository;

import com.unicamp.porteirobackend.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
}
