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
    public ResponseEntity<?> getAllVisitor() {
        List<VisitorDTO> visitors;
        try {
            visitors = porteiroService.getAllVisitors();
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        }
        return ResponseEntity.ok(visitors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVisitorById(@PathVariable Integer id) {
        VisitorDTO visitor;
        try {
            visitor = porteiroService.getVisitorById(id);
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        }
        return ResponseEntity.ok(visitor);
    }

    @PostMapping
    public ResponseEntity<?> createVisitor(@RequestBody VisitorDTO visitorRequest) {
        VisitorDTO visitor;
        try {
            visitor = porteiroService.createVisitor(visitorRequest);
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        }
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/{id}").buildAndExpand(visitor.getId()).toUri()).body(visitor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVisitor(@PathVariable Integer id, @RequestBody VisitorDTO visitorRequest) {
        VisitorDTO visitor;
        try {
            visitor = porteiroService.updateVisitor(id, visitorRequest);
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        }
        return ResponseEntity.ok(visitor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVisitor(@PathVariable Integer id) {
        try {
            porteiroService.deleteVisitor(id);
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        }
        return ResponseEntity.noContent().build();
    }

}