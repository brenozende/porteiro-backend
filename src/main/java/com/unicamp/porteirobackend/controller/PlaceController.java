package com.unicamp.porteirobackend.controller;


import com.unicamp.porteirobackend.dto.PlaceDTO;
import com.unicamp.porteirobackend.entity.Place;
import com.unicamp.porteirobackend.exception.PorteiroException;
import com.unicamp.porteirobackend.repository.PlaceRepository;
import com.unicamp.porteirobackend.service.PorteiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    @Autowired
    PorteiroService porteiroService;


    @GetMapping
    public ResponseEntity<?> getAllPlaces() {
        List<PlaceDTO> places = porteiroService.getAllPlaces();
        if (places.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(places);
    }

    @PreAuthorize("hasAuthority('ADM')")
    @PostMapping
    public ResponseEntity<?> createPlace(@RequestBody PlaceDTO placeRequest) {
        PlaceDTO place = porteiroService.createPlace(placeRequest);
        if (place == null)
            return ResponseEntity.internalServerError().build();
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/").buildAndExpand(place.getId()).toUri()).body(place);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlaceById(@PathVariable Integer id) {
        PlaceDTO place = porteiroService.getPlaceById(id);
        if (place == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(place);
    }

    @PreAuthorize("hasAuthority('ADM')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlace(@PathVariable Integer id, @RequestBody PlaceDTO placeUpdateRequest) {
        PlaceDTO place = porteiroService.updatePlace(id, placeUpdateRequest);
        if (place == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(place);
    }

    @PreAuthorize("hasAuthority('ADM')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable Integer id) {
        try {
            porteiroService.deletePlace(id);
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        }
        return ResponseEntity.noContent().build();
    }
}

