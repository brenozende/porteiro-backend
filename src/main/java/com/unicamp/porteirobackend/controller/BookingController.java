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

    @Autowired
    private BookingRepository repository;

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<Booking> bookings = repository.findAll();
        if (bookings.isEmpty())
            return ResponseEntity.notFound().build();

        List<BookingDTO> bookingDTOS = new ArrayList<>();
        bookings.forEach(b -> {
            BookingDTO dto = new BookingDTO(b);
            bookingDTOS.add(dto);
        });
        return ResponseEntity.ok(bookingDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Integer id) {
        Optional<Booking> optionalBooking = repository.findById(id);
        if (optionalBooking.isEmpty())
            return ResponseEntity.notFound().build();

        BookingDTO bookingDTO = new BookingDTO(optionalBooking.get());
        return ResponseEntity.ok(bookingDTO);
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDTO bookingRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = porteiroService.getUser(userDetails);
        BookingDTO bookingDTO;
        try {
            bookingDTO = porteiroService.createBooking(bookingRequest, user);
        } catch (BookingCreationException e) {
            return ResponseEntity.badRequest().body(e.getErrorMsg());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.created(UriComponentsBuilder.fromPath("/{id}").buildAndExpand(bookingDTO.getId()).toUri()).body(bookingDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Integer id, @RequestBody BookingDTO bookingRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = porteiroService.getUser(userDetails);
        BookingDTO bookingDTO;
        try {
            bookingDTO = porteiroService.updateBooking(id, bookingRequest, user);
        } catch (BookingCreationException e) {
            return ResponseEntity.badRequest().body(e.getErrorMsg());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(bookingDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Integer id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = porteiroService.getUser(userDetails);

        try {
            porteiroService.deleteBooking(id, user);
        } catch (BookingDeletionException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getErrorMsg());
        }

        return ResponseEntity.noContent().build();

    }
}

