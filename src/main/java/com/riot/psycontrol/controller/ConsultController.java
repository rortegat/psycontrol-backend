package com.riot.psycontrol.controller;

import com.riot.psycontrol.dto.ConsultDTO;
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
    public List<ConsultDTO> listConsults(@PathVariable Integer id) {
        return consultService.getConsultsByPatientId(id);
    }

    @GetMapping("/{id}")
    public ConsultDTO getConsult(@PathVariable Integer id) {
        return consultService.getConsult(id);
    }

    @PostMapping("/add")
    public ConsultDTO addConsult(@RequestBody ConsultDTO consultDTO) {
        return consultService.saveConsult(consultDTO);
    }

    @PutMapping("/update")
    public ConsultDTO updateConsult(@RequestBody ConsultDTO consultDTO) {
        return consultService.updateConsult(consultDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteConsult(@PathVariable Integer id) {
        consultService.deleteConsult(id);
    }
}
