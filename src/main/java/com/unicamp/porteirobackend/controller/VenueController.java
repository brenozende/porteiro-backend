package com.unicamp.porteirobackend.controller;


import com.unicamp.porteirobackend.entity.Place;
import com.unicamp.porteirobackend.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/venues")
public class VenueController {

    @Autowired
    private PlaceRepository repository;

    @GetMapping
    public List<Place> getAllVenue() {
        return repository.findAll();
    }

    @PostMapping
    public Place createVenue(@RequestBody Place place) {
        return repository.save(place);
    }

    @GetMapping("/{id}")
    public Place getVenueById(@PathVariable Integer id) {
        Optional<Place> optionalVenue = repository.findById(id);
        return optionalVenue.orElse(null);
    }

    @PutMapping("/{id}")
    public Place updateVenue(@PathVariable Integer id, @RequestBody Place place) {
        Optional<Place> optionalVenue = repository.findById(id);
        if (optionalVenue.isPresent()) {
            place.setId(id);
            return repository.save(place);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteVenue(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}

