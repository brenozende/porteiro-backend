package com.unicamp.porteirobackend.controller;

import java.util.List;
import java.util.Optional;

import com.unicamp.porteirobackend.entity.Reservation;
import com.unicamp.porteirobackend.repository.ReservationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationRepository repository;

    @GetMapping
    public List<Reservation> getAllReservations() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable Integer id) {
        Optional<Reservation> optionalReservation = repository.findById(id);
        return optionalReservation.orElse(null);
    }

    @PostMapping
    public Reservation createReservation(@RequestBody Reservation reservation) {
        return repository.save(reservation);
    }

    @PutMapping("/{id}")
    public Reservation updateReservation(@PathVariable Integer id, @RequestBody Reservation reservation) {
        Optional<Reservation> optionalReservation = repository.findById(id);
        if (optionalReservation.isPresent()) {
            Reservation existingReservation = optionalReservation.get();
            existingReservation.setVenue(reservation.getVenue());
            existingReservation.setUser(reservation.getUser());
            existingReservation.setReservationFrom(reservation.getReservationFrom());
            existingReservation.setReservationTo(reservation.getReservationTo());
            return repository.save(existingReservation);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}

