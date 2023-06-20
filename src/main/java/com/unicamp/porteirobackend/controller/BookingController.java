package com.unicamp.porteirobackend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.unicamp.porteirobackend.dto.BookingDTO;
import com.unicamp.porteirobackend.entity.Booking;
import com.unicamp.porteirobackend.entity.Place;
import com.unicamp.porteirobackend.entity.User;
import com.unicamp.porteirobackend.exception.BookingCreationException;
import com.unicamp.porteirobackend.exception.BookingDeletionException;
import com.unicamp.porteirobackend.exception.PorteiroException;
import com.unicamp.porteirobackend.repository.BookingRepository;

import com.unicamp.porteirobackend.security.services.UserDetailsImpl;
import com.unicamp.porteirobackend.service.PorteiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/reservations")
public class BookingController {

    @Autowired
    private PorteiroService porteiroService;

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<BookingDTO> bookings = porteiroService.getAllBookings();
        if (bookings.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Integer id) {
        BookingDTO booking = porteiroService.getBookingById(id);
        if (booking == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(booking);
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDTO bookingRequest) {
        BookingDTO bookingDTO;
        try {
            bookingDTO = porteiroService.createBooking(bookingRequest);
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.created(UriComponentsBuilder.fromPath("/{id}").buildAndExpand(bookingDTO.getId()).toUri()).body(bookingDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Integer id, @RequestBody BookingDTO bookingRequest) {
        BookingDTO bookingDTO;
        try {
            bookingDTO = porteiroService.updateBooking(id, bookingRequest);
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(bookingDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Integer id) {
        try {
            porteiroService.deleteBooking(id);
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        }
        return ResponseEntity.noContent().build();
    }
}

