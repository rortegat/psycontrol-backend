package com.riot.psycontrol.controller;

import com.riot.psycontrol.dto.PatientDTO;
import com.riot.psycontrol.service.IPatientService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    @Qualifier("patientServiceImpl")
    IPatientService patientService;

    @ApiOperation(value = "View a list of available patients", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping("/all")
    public Page<PatientDTO> listPatients(Pageable pageable, Principal principal){
        return patientService.getPagePatients(pageable, principal.getName());
    }

    @GetMapping("/{id}")
    public PatientDTO getPatient(@PathVariable Integer id){
        return patientService.getPatientById(id);
    }

    @PostMapping("/add")
    public PatientDTO addPatient(@RequestBody PatientDTO patient){
        return patientService.savePatient(patient);
    }

    @PutMapping("/update")
    public PatientDTO updatePatient(@RequestBody PatientDTO patient){
        return patientService.updatePatient(patient);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePatient(@PathVariable Integer id){
        patientService.removePatient(id);
    }

}
