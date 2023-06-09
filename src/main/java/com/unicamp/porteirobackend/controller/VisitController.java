package com.unicamp.porteirobackend.controller;

import com.unicamp.porteirobackend.entity.Visit;
import com.unicamp.porteirobackend.enums.EVisitStatus;
import com.unicamp.porteirobackend.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    @Autowired
    private VisitRepository repository;

    @GetMapping
    public List<Visit> getAllVisit() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Visit getVisitById(@PathVariable Integer id) {
        Optional<Visit> optionalVisit = repository.findById(id);
        return optionalVisit.orElse(null);
    }

    @PostMapping
    public Visit createVisit(@RequestBody Visit visit) {
        return repository.save(visit);
    }

    @PutMapping("/{id}")
    public Visit updateVisit(@PathVariable Integer id, @RequestBody Visit visit) {
        Optional<Visit> optionalVisit = repository.findById(id);
        if (optionalVisit.isPresent()) {
            visit.setId(id);
            return repository.save(visit);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteVisit(@PathVariable Integer id) {
        Optional<Visit> optionalVisit = repository.findById(id);
        optionalVisit.ifPresent(repository::delete);
    }

    @GetMapping("/status/{status}")
    public List<Visit> getVisitByStatus(@PathVariable EVisitStatus status) {
        return repository.findByVisitStatus(status);
    }

}