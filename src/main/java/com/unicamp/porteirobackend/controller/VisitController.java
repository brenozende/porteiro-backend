package com.unicamp.porteirobackend.controller;

import com.unicamp.porteirobackend.dto.VisitDTO;
import com.unicamp.porteirobackend.entity.User;
import com.unicamp.porteirobackend.enums.EVisitStatus;
import com.unicamp.porteirobackend.exception.BookingCreationException;
import com.unicamp.porteirobackend.exception.PorteiroException;
import com.unicamp.porteirobackend.repository.VisitRepository;
import com.unicamp.porteirobackend.repository.VisitorRepository;
import com.unicamp.porteirobackend.security.services.UserDetailsImpl;
import com.unicamp.porteirobackend.service.PorteiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@CrossOrigin(allowedHeaders = {"Authorization"})
@RestController
@RequestMapping("/api/visits")
public class VisitController {

    @Autowired
    PorteiroService porteiroService;

    @Autowired
    private VisitRepository repository;
    @Autowired
    private VisitorRepository visitorRepository;

    @GetMapping
    public ResponseEntity<?> getAllVisit() {
        List<VisitDTO> visits;
        try {
            visits = porteiroService.getVisits();
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        }
        if (visits.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVisitById(@PathVariable Integer id) {
        VisitDTO visit;
        try {
            visit = porteiroService.getVisitById(id);
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        }

        if (visit == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(visit);
    }

    @PostMapping
    public ResponseEntity<?> createVisit(@RequestBody VisitDTO visitRequest) {
        VisitDTO visit;
        try {
            visit = porteiroService.createVisit(visitRequest);
        } catch (BookingCreationException e) {
            return ResponseEntity.internalServerError().body(e.getErrorMsg());
        }

        return ResponseEntity.created(UriComponentsBuilder.fromPath("/")
                .buildAndExpand(visit.getId()).toUri()).body(visit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVisit(@PathVariable Integer id, @RequestBody VisitDTO visitRequest) {
        VisitDTO visit;
        try {
            visit = porteiroService.updateVisit(id, visitRequest);
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        }

        return ResponseEntity.ok(visit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVisit(@PathVariable Integer id) {
        try {
            porteiroService.deleteVisit(id);
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getVisitByStatus(@PathVariable EVisitStatus status) {
        List<VisitDTO> visits;
        try {
            visits = porteiroService.findVisitByStatus(status);
        } catch (BookingCreationException e) {
            return ResponseEntity.internalServerError().body(e.getErrorMsg());
        }
        if (visits.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(visits);
    }

}