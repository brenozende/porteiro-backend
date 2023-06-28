package com.unicamp.porteirobackend.controller;

import com.unicamp.porteirobackend.dto.ResidentDTO;
import com.unicamp.porteirobackend.dto.VisitorDTO;
import com.unicamp.porteirobackend.dto.request.RegisterForm;
import com.unicamp.porteirobackend.service.PorteiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;

@CrossOrigin(allowedHeaders = {"Authorization"})
@RestController
@RequestMapping("/api/resident")
public class ResidentController {

    @Autowired
    PorteiroService porteiroService;

    @PreAuthorize("hasAnyAuthority('ADM', 'CON')")
    @GetMapping
    public ResponseEntity<List<ResidentDTO>> getAllResidents() {
        List<ResidentDTO> visitors = porteiroService.getAllResidents();
        return ResponseEntity.ok(visitors);
    }

    @PreAuthorize("hasAnyAuthority('ADM', 'CON')")
    @PostMapping("/register")
    public ResponseEntity<ResidentDTO> registerResident(@RequestBody RegisterForm form){
        ResidentDTO resident = porteiroService.registerResident(form);
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/{id}").buildAndExpand(resident.getId()).toUri()).body(resident);
    }

    @PreAuthorize("hasAnyAuthority('ADM', 'CON')")
    @PutMapping("/{residentId}/visitors/add")
    public ResponseEntity<ResidentDTO> addVisitors(@RequestBody List<VisitorDTO> visitors,
                                                @PathVariable Integer residentId) {

        ResidentDTO resident = porteiroService.addVisitors(residentId, visitors);
        if (resident == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(resident);
    }

    @GetMapping("/{residentId}/visitors")
    public ResponseEntity<List<VisitorDTO>> findVisitorsByResidentId(@PathVariable Integer residentId) {

        List<VisitorDTO> visitors = porteiroService.findVisitorsByResident(residentId);

        if (visitors.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(visitors);
    }
}
