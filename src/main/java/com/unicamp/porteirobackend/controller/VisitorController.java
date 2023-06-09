package com.unicamp.porteirobackend.controller;

import com.unicamp.porteirobackend.entity.Visitor;
import com.unicamp.porteirobackend.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitors")
public class VisitorController {

    @Autowired
    private VisitorRepository visitorRepository;

    @GetMapping
    public List<Visitor> getAllVisitor() {
        return visitorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Visitor getVisitorById(@PathVariable Integer id) {
        return visitorRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Visitor createVisitor(@RequestBody Visitor visitor) {
        return visitorRepository.save(visitor);
    }

    @PutMapping("/{id}")
    public Visitor updateVisitor(@PathVariable Integer id, @RequestBody Visitor visitor) {
        visitor.setId(id);
        return visitorRepository.save(visitor);
    }

    @DeleteMapping("/{id}")
    public void deleteVisitor(@PathVariable Integer id) {
        visitorRepository.deleteById(id);
    }

}