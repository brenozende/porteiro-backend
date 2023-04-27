package com.unicamp.porteirobackend.controller;


import com.unicamp.porteirobackend.entity.Venue;
import com.unicamp.porteirobackend.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/venues")
public class VenueController {

    @Autowired
    private VenueRepository repository;

    @GetMapping
    public List<Venue> getAllVenue() {
        return repository.findAll();
    }

    @PostMapping
    public Venue createVenue(@RequestBody Venue venue) {
        return repository.save(venue);
    }

    @GetMapping("/{id}")
    public Venue getVenueById(@PathVariable Integer id) {
        Optional<Venue> optionalVenue = repository.findById(id);
        return optionalVenue.orElse(null);
    }

    @PutMapping("/{id}")
    public Venue updateVenue(@PathVariable Integer id, @RequestBody Venue venue) {
        Optional<Venue> optionalVenue = repository.findById(id);
        if (optionalVenue.isPresent()) {
            venue.setId(id);
            return repository.save(venue);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteVenue(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}

