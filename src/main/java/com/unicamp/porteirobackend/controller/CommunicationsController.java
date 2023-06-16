package com.unicamp.porteirobackend.controller;

import com.unicamp.porteirobackend.dto.CommunicationDTO;
import com.unicamp.porteirobackend.entity.Role;
import com.unicamp.porteirobackend.entity.User;
import com.unicamp.porteirobackend.enums.EUserRole;
import com.unicamp.porteirobackend.repository.CommunicationsRepository;
import com.unicamp.porteirobackend.entity.Communications;
import com.unicamp.porteirobackend.security.services.UserDetailsImpl;
import com.unicamp.porteirobackend.service.PorteiroService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
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
        List<Communications> communications = repository.findAll();
        if (communications.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(communications.stream().map(CommunicationDTO::new).toList());
    }

    @PreAuthorize("hasAnyAuthority('ADM', 'CON')")
    @PostMapping
    public ResponseEntity<?> createCommunication(@RequestBody CommunicationDTO request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = porteiroService.getUser(userDetails);
        Communications communications = new Communications();
        communications.setFromUser(user);
        communications.setDate(new Date());
        communications.setMessage(request.getMessage());
        repository.save(communications);
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/{id}").buildAndExpand(communications.getId()).toUri()).body(new CommunicationDTO(communications));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommunicationById(@PathVariable Integer id){
        Optional<Communications> optionsCommunication = repository.findById(id);
        if (optionsCommunication.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new CommunicationDTO(optionsCommunication.get()));
    }

    @PreAuthorize("hasAnyAuthority('ADM', 'CON')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCommunications(@PathVariable Integer id, @RequestBody CommunicationDTO communication){
        Optional<Communications> optionalCommunications = repository.findById(id);
        if(optionalCommunications.isPresent()){
            Communications existingCommunication = optionalCommunications.get();
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = porteiroService.getUser(userDetails);
            if (!existingCommunication.getFromUser().equals(user))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cannot update other user's communication");

            existingCommunication.setMessage(communication.getMessage());
            existingCommunication.setDate(new Date());
            repository.save(existingCommunication);
            return ResponseEntity.ok(new CommunicationDTO(existingCommunication));
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('ADM', 'CON')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommunication(@PathVariable Integer id){
        Optional<Communications> optionalCommunications = repository.findById(id);
        if(optionalCommunications.isPresent()) {
            Communications existingCommunication = optionalCommunications.get();
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = porteiroService.getUser(userDetails);
            if (!existingCommunication.getFromUser().equals(user))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cannot delete other user's communication");
            repository.deleteById(id);
        }
        return ResponseEntity.notFound().build();
    }
}
