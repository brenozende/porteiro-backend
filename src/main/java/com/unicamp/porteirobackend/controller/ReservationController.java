package com.unicamp.porteirobackend.controller;

import java.util.List;
import java.util.Optional;

import com.unicamp.porteirobackend.entity.Booking;
import com.unicamp.porteirobackend.repository.ReservationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationRepository repository;

    @GetMapping
    public List<Booking> getAllReservations() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Booking getReservationById(@PathVariable Integer id) {
        Optional<Booking> optionalReservation = repository.findById(id);
        return optionalReservation.orElse(null);
    }

    @PostMapping
    public Booking createReservation(@RequestBody Booking booking) {
        return repository.save(booking);
    }

    @PutMapping("/{id}")
    public Booking updateReservation(@PathVariable Integer id, @RequestBody Booking booking) {
        Optional<Booking> optionalReservation = repository.findById(id);
        if (optionalReservation.isPresent()) {
            Booking existingBooking = optionalReservation.get();
            existingBooking.setPlace(booking.getPlace());
            existingBooking.setResident(booking.getResident());
            existingBooking.setReservationFrom(booking.getReservationFrom());
            existingBooking.setReservationTo(booking.getReservationTo());
            return repository.save(existingBooking);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}

