package com.unicamp.porteirobackend.controller;


import com.unicamp.porteirobackend.dto.PlaceDTO;
import com.unicamp.porteirobackend.entity.Place;
import com.unicamp.porteirobackend.repository.PlaceRepository;
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
    private PlaceRepository repository;

    @GetMapping
    public List<Place> getAllPlaces() {
        return repository.findAll();
    }

    @PreAuthorize("hasAuthority('ADM')")
    @PostMapping
    public ResponseEntity<Place> createPlace(@RequestBody PlaceDTO placeRequest) {
        Place place = new Place();
        place.setName(placeRequest.getName());
        place.setBlocked(false);
        repository.save(place);
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/").buildAndExpand(place.getId()).toUri()).body(place);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Integer id) {
        Optional<Place> optionalPlace = repository.findById(id);
        return optionalPlace.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('ADM')")
    @PutMapping("/{id}")
    public ResponseEntity<Place> updatePlace(@PathVariable Integer id, @RequestBody PlaceDTO placeUpdateRequest) {
        Optional<Place> optionalPlace = repository.findById(id);
        if (optionalPlace.isPresent()) {
            Place place = optionalPlace.get();
            place.setName(placeUpdateRequest.getName());
            place.setBlocked(placeUpdateRequest.getBlocked());
            repository.save(place);
            return ResponseEntity.ok(place);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('ADM')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVenue(@PathVariable Integer id) {
        Optional<Place> placeOptional = repository.findById(id);
        if (placeOptional.isEmpty())
            return ResponseEntity.notFound().build();
        repository.delete(placeOptional.get());
        return ResponseEntity.noContent().build();
    }
}

