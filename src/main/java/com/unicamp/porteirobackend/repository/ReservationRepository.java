package com.unicamp.porteirobackend.repository;

import com.unicamp.porteirobackend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Booking, Integer> {
}
