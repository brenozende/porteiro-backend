package com.unicamp.porteirobackend.controller;

import com.unicamp.porteirobackend.dto.VisitDTO;
import com.unicamp.porteirobackend.enums.EVisitStatus;
import com.unicamp.porteirobackend.exception.PorteiroException;
import com.unicamp.porteirobackend.repository.VisitRepository;
import com.unicamp.porteirobackend.repository.VisitorRepository;
import com.unicamp.porteirobackend.service.PorteiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<VisitDTO>> getAllVisit() {
        List<VisitDTO> visits;
        visits = porteiroService.getVisits();
        if (visits.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitDTO> getVisitById(@PathVariable Integer id) {
        VisitDTO visit;
        visit = porteiroService.getVisitById(id);

        if (visit == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(visit);
    }

    @PostMapping
    public ResponseEntity<VisitDTO> createVisit(@RequestBody VisitDTO visitRequest) {
        VisitDTO visit = porteiroService.createVisit(visitRequest);
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/")
                .buildAndExpand(visit.getId()).toUri()).body(visit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitDTO> updateVisit(@PathVariable Integer id, @RequestBody VisitDTO visitRequest) {
        VisitDTO visit;
        visit = porteiroService.updateVisit(id, visitRequest);

        return ResponseEntity.ok(visit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisit(@PathVariable Integer id) {

        porteiroService.deleteVisit(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<VisitDTO>> getVisitByStatus(@PathVariable EVisitStatus status) {
        List<VisitDTO> visits;
        visits = porteiroService.findVisitByStatus(status);
        if (visits.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(visits);
    }

}