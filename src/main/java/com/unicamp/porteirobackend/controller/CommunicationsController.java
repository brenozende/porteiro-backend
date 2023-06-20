package com.unicamp.porteirobackend.controller;

import com.unicamp.porteirobackend.dto.CommunicationDTO;
import com.unicamp.porteirobackend.exception.PorteiroException;
import com.unicamp.porteirobackend.repository.CommunicationsRepository;
import com.unicamp.porteirobackend.service.PorteiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

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
    public ResponseEntity<CommunicationDTO> createCommunication(@RequestBody CommunicationDTO request) {
        CommunicationDTO communication = porteiroService.createCommunication(request);
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/{id}").buildAndExpand(communication.getId()).toUri()).body(communication);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunicationDTO> getCommunicationById(@PathVariable Integer id){
        CommunicationDTO communication = porteiroService.getCommunicationById(id);
        if (communication == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(communication);
    }

    @PreAuthorize("hasAnyAuthority('ADM', 'CON')")
    @PutMapping("/{id}")
    public ResponseEntity<CommunicationDTO> updateCommunications(@PathVariable Integer id, @RequestBody CommunicationDTO communicationRequest){
        CommunicationDTO communication = porteiroService.updateCommunication(id, communicationRequest);
        return ResponseEntity.ok(communication);
    }

    @PreAuthorize("hasAnyAuthority('ADM', 'CON')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunication(@PathVariable Integer id) {
        porteiroService.deleteCommunication(id);
        return ResponseEntity.noContent().build();
    }
}
