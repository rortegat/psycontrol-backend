package com.riot.psycontrol.controller;

import com.riot.psycontrol.dao.Patient;
import com.riot.psycontrol.service.PatientService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    PatientService patientService;


    @ApiOperation(value = "View a list of available patients", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping("/all")
    public List<Patient> listPatients(){
        return patientService.getPatients();
    }

    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable Integer id){
        return patientService.getPatientById(id);
    }

    @PostMapping("/add")
    public Patient addPatient(@RequestBody Patient patient){
        return patientService.savePatient(patient);
    }

    @PutMapping("/update")
    public Patient updatePatient(@RequestBody Patient patient){
        return patientService.updatePatient(patient);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePatient(@PathVariable Integer id){
        patientService.removePatient(id);
    }

}
