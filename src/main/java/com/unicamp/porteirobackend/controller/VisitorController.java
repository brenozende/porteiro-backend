package com.unicamp.porteirobackend.controller;

import com.unicamp.porteirobackend.dto.VisitorDTO;
import com.unicamp.porteirobackend.exception.PorteiroException;
import com.unicamp.porteirobackend.service.PorteiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/visitors")
public class VisitorController {

    @Autowired
    private PorteiroService porteiroService;

    @GetMapping
    public ResponseEntity<List<VisitorDTO>> getAllVisitor() {
        List<VisitorDTO> visitors = porteiroService.getAllVisitors();
        return ResponseEntity.ok(visitors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitorDTO> getVisitorById(@PathVariable Integer id) {
        VisitorDTO visitor = porteiroService.getVisitorById(id);
        return ResponseEntity.ok(visitor);
    }

    @PostMapping
    public ResponseEntity<VisitorDTO> createVisitor(@RequestBody VisitorDTO visitorRequest) {
        VisitorDTO visitor = porteiroService.createVisitor(visitorRequest);
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/{id}").buildAndExpand(visitor.getId()).toUri()).body(visitor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitorDTO> updateVisitor(@PathVariable Integer id, @RequestBody VisitorDTO visitorRequest) {
        VisitorDTO visitor = porteiroService.updateVisitor(id, visitorRequest);
        return ResponseEntity.ok(visitor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisitor(@PathVariable Integer id) {
        porteiroService.deleteVisitor(id);
        return ResponseEntity.noContent().build();
    }

}