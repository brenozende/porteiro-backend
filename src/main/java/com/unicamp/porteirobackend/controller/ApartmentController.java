package com.unicamp.porteirobackend.controller;

import com.unicamp.porteirobackend.dto.ApartmentDTO;
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
    public ResponseEntity<ApartmentDTO> createApartment(@RequestBody ApartmentDTO apartmentRequest) {
        ApartmentDTO apartment = porteiroService.createApartment(apartmentRequest);
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/{id}").buildAndExpand(apartment.getId()).toUri()).body(apartment);
    }

    @PreAuthorize("hasAuthority('ADM')")
    @PutMapping("/{id}")
    public ResponseEntity<ApartmentDTO> updateApartment(@PathVariable Integer id, @RequestBody ApartmentDTO apartmentRequest) {
        ApartmentDTO apartment = porteiroService.updateApartment(id, apartmentRequest);
        return ResponseEntity.ok(apartment);
    }

    @PreAuthorize("hasAuthority('ADM')")
    @GetMapping
    public ResponseEntity<List<ApartmentDTO>> getApartments() {
        List<ApartmentDTO> apartments =  porteiroService.getApartments();
        return ResponseEntity.ok(apartments);
    }

    @PreAuthorize("hasAuthority('ADM')")
    @GetMapping("/{id}")
    public ResponseEntity<ApartmentDTO> getApartmentById(@PathVariable Integer id) {
        ApartmentDTO apartment = porteiroService.getApartmentById(id);
        return ResponseEntity.ok(apartment);
    }

    @PreAuthorize("hasAuthority('ADM')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApartment(@PathVariable Integer id) {
        porteiroService.deleteApartment(id);
        return ResponseEntity.noContent().build();
    }


}
