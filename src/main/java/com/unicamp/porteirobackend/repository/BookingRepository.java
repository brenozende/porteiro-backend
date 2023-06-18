package com.unicamp.porteirobackend.repository;

import com.unicamp.porteirobackend.entity.Booking;
import com.unicamp.porteirobackend.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Booking findByPlace(Place place);
}
