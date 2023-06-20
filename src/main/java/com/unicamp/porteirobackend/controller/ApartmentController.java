package com.unicamp.porteirobackend.controller;

import com.unicamp.porteirobackend.dto.ApartmentDTO;
import com.unicamp.porteirobackend.dto.VisitorDTO;
import com.unicamp.porteirobackend.exception.PorteiroException;
import com.unicamp.porteirobackend.service.PorteiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/apartments")
public class ApartmentController {

    @Autowired
    PorteiroService porteiroService;

    @PreAuthorize("hasAuthority('ADM')")
    @PostMapping
    public ResponseEntity<?> createApartment(@RequestBody ApartmentDTO apartmentRequest) {
        ApartmentDTO apartment;
        try {
            apartment = porteiroService.createApartment(apartmentRequest);
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        }
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/{id}").buildAndExpand(apartment.getId()).toUri()).body(apartment);
    }

    @PreAuthorize("hasAuthority('ADM')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateApartment(@PathVariable Integer id, @RequestBody ApartmentDTO apartmentRequest) {
        ApartmentDTO apartment;
        try {
            apartment = porteiroService.updateApartment(id, apartmentRequest);
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        }
        return ResponseEntity.ok(apartment);
    }

    @PreAuthorize("hasAuthority('ADM')")
    @GetMapping
    public ResponseEntity<?> getApartments() {
        List<ApartmentDTO> apartments;
        try {
            apartments = porteiroService.getApartments();
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        }
        return ResponseEntity.ok(apartments);
    }

    @PreAuthorize("hasAuthority('ADM')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getApartmentById(@PathVariable Integer id) {
        ApartmentDTO apartment;
        try {
            apartment = porteiroService.getApartmentById(id);
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        }
        return ResponseEntity.ok(apartment);
    }

    @PreAuthorize("hasAuthority('ADM')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApartment(@PathVariable Integer id) {
        try {
            porteiroService.deleteApartment(id);
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        }
        return ResponseEntity.noContent().build();
    }


}
