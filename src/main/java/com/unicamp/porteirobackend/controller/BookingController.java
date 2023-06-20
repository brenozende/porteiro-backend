package com.unicamp.porteirobackend.controller;

import java.util.List;

import com.unicamp.porteirobackend.dto.BookingDTO;
import com.unicamp.porteirobackend.exception.PorteiroException;

import com.unicamp.porteirobackend.service.PorteiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO bookingRequest) {

        BookingDTO bookingDTO = porteiroService.createBooking(bookingRequest);

        return ResponseEntity.created(UriComponentsBuilder.fromPath("/{id}").buildAndExpand(bookingDTO.getId()).toUri()).body(bookingDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable Integer id, @RequestBody BookingDTO bookingRequest) {
        BookingDTO bookingDTO = porteiroService.updateBooking(id, bookingRequest);
        return ResponseEntity.ok(bookingDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Integer id) {
        porteiroService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}

