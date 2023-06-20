package com.unicamp.porteirobackend.controller;

import com.unicamp.porteirobackend.dto.CommunicationDTO;
import com.unicamp.porteirobackend.entity.User;
import com.unicamp.porteirobackend.exception.PorteiroException;
import com.unicamp.porteirobackend.repository.CommunicationsRepository;
import com.unicamp.porteirobackend.entity.Communications;
import com.unicamp.porteirobackend.security.services.UserDetailsImpl;
import com.unicamp.porteirobackend.service.PorteiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/communications")
public class CommunicationsController {

    @Autowired
    PorteiroService porteiroService;

    @Autowired
    private CommunicationsRepository repository;

    @GetMapping
    public ResponseEntity<List<CommunicationDTO>> getAllCommunications(){
        List<CommunicationDTO> communications = porteiroService.getAllCommunications();
        if (communications.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(communications);
    }

    @PreAuthorize("hasAnyAuthority('ADM', 'CON')")
    @PostMapping
    public ResponseEntity<?> createCommunication(@RequestBody CommunicationDTO request) {
        CommunicationDTO communication = porteiroService.createCommunication(request);
        if (communication == null)
            return ResponseEntity.internalServerError().body("Error while creating communication");

        return ResponseEntity.created(UriComponentsBuilder.fromPath("/{id}").buildAndExpand(communication.getId()).toUri()).body(communication);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommunicationById(@PathVariable Integer id){
        CommunicationDTO communication = porteiroService.getCommunicationById(id);
        if (communication == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(communication);
    }

    @PreAuthorize("hasAnyAuthority('ADM', 'CON')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCommunications(@PathVariable Integer id, @RequestBody CommunicationDTO communicationRequest){
        CommunicationDTO communication;
        try {
            communication = porteiroService.updateCommunication(id, communicationRequest);
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        }
        return ResponseEntity.ok(communication);
    }

    @PreAuthorize("hasAnyAuthority('ADM', 'CON')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommunication(@PathVariable Integer id) {
        try {
            porteiroService.deleteCommunication(id);
        } catch (PorteiroException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMsg());
        }
        return ResponseEntity.noContent().build();
    }
}
