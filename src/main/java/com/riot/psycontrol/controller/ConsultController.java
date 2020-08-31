package com.riot.psycontrol.controller;

import com.riot.psycontrol.dto.ConsultDTO;
import com.riot.psycontrol.service.IPatientService;
import com.riot.psycontrol.service.impl.ConsultServiceImpl;
import com.riot.psycontrol.service.impl.PatientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consults")
public class ConsultController {

    @Autowired
    ConsultServiceImpl consultServiceImpl;

    @Autowired
    @Qualifier("patientServiceImpl")
    IPatientService patientService;

    @GetMapping("/patient/{id}")
    public List<ConsultDTO> listConsults(@PathVariable Integer id) {
        return consultServiceImpl.getConsultsByPatientId(id);
    }

    @GetMapping("/{id}")
    public ConsultDTO getConsult(@PathVariable Integer id) {
        return consultServiceImpl.getConsult(id);
    }

    @PostMapping("/add")
    public ConsultDTO addConsult(@RequestBody ConsultDTO consultDTO) {
        return consultServiceImpl.saveConsult(consultDTO);
    }

    @PutMapping("/update")
    public ConsultDTO updateConsult(@RequestBody ConsultDTO consultDTO) {
        return consultServiceImpl.updateConsult(consultDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteConsult(@PathVariable Integer id) {
        consultServiceImpl.deleteConsult(id);
    }
}
