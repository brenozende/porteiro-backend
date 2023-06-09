package com.unicamp.porteirobackend.controller;

import com.unicamp.porteirobackend.repository.CommunicationsRepository;
import com.unicamp.porteirobackend.entity.Communications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/communications")
public class CommunicationsController {

    @Autowired
    private CommunicationsRepository repository;

    @GetMapping
    public List<Communications> getAllCommunications(){
        return repository.findAll();
    }

    @PostMapping
    public Communications createCommunication(@RequestBody Communications communication){
        return repository.save(communication);
    }

    @GetMapping("/{id}")
    public Communications getCommunicationById(@PathVariable Integer id){
        Optional<Communications> optionsCommunication = repository.findById(id);
        return optionsCommunication.orElse(null);
    }

    @PutMapping("/{id}")
    public Communications updateCommunications(@PathVariable Integer id, @RequestBody Communications communication){
        Optional<Communications> optionalCommunications = repository.findById(id);
        if(optionalCommunications.isPresent()){
            Communications existingCommunication = optionalCommunications.get();
            existingCommunication.setFromUser(communication.getFromUser());
            existingCommunication.setMessage(communication.getMessage());
            existingCommunication.setDate(communication.getDate());
            return repository.save(existingCommunication);
        }else{
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCommunication(@PathVariable Integer id){
        repository.deleteById(id);
    }
}
