package com.riot.psycontrol.controller;

import com.riot.psycontrol.entity.Consult;
import com.riot.psycontrol.service.ConsultService;
import com.riot.psycontrol.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consults")
public class ConsultController {

    @Autowired
    ConsultService consultService;

    @Autowired
    PatientService patientService;

    @GetMapping("/patient/{id}")
    public List<Consult> listConsults(@PathVariable Integer id){
        return consultService.getConsultsByPatientId(id);
    }

    @GetMapping("/{id}")
    public Consult getConsult(@PathVariable Integer id){
        return consultService.getConsult(id);
    }

    @PostMapping("/add/{patientId}")
    public Consult addConsult(@PathVariable Integer patientId, @RequestBody Consult consult){
        return consultService.saveConsult(patientId, consult);
    }

    @PutMapping("/update")
    public Consult updateConsult(@RequestBody Consult consult){
        return consultService.updateConsult(consult);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> removeConsult(@PathVariable Integer id){
        consultService.deleteConsult(id);
        return ResponseEntity.ok("Deleted");
    }
}
