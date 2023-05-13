package com.unicamp.porteirobackend.controller;

import com.unicamp.porteirobackend.dto.request.RegisterForm;
import com.unicamp.porteirobackend.entity.Resident;
import com.unicamp.porteirobackend.entity.User;
import com.unicamp.porteirobackend.security.services.UserDetailsImpl;
import com.unicamp.porteirobackend.service.PorteiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/resident")
public class ResidentController {

    @Autowired
    PorteiroService porteiroService;

    @PostMapping("/register")
    public ResponseEntity<Resident> registerResident(@RequestBody RegisterForm form){
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        form.setFilledBy(user.getUsername());

        Resident resident = porteiroService.registerResident(form);
        if (resident == null)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.created(UriComponentsBuilder.fromPath("/{id}").buildAndExpand(resident.getId()).toUri()).body(resident);
    }
}
