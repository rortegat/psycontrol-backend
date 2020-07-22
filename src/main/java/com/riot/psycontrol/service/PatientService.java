package com.riot.psycontrol.service;

import com.riot.psycontrol.dao.Patient;
import com.riot.psycontrol.repo.PatientRepo;
import com.riot.psycontrol.repo.UserRepo;
import com.riot.psycontrol.security.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    PatientRepo patientRepo;

    @Autowired
    UserService userService;

    public List<Patient> getPatients(String username) {
        if (username != null)
            return patientRepo.findAllByCreatedBy(username);
        else
            throw new CustomException("There is no username", HttpStatus.BAD_REQUEST);
    }

    public Patient getPatientById(Integer id) {
        return patientRepo.findById(id).get();
    }

    public Patient savePatient(Patient patient) {
        return patientRepo.save(patient);
    }

    public Patient updatePatient(Patient patient) {
        Patient updated = patientRepo.findById(patient.getId()).get();
        updated.setFirstname(patient.getFirstname());
        updated.setLastname(patient.getLastname());
        updated.setEmail(patient.getEmail());
        updated.setPhone(patient.getPhone());
        updated.setMobile(patient.getMobile());
        return patientRepo.save(updated);
    }

    public void removePatient(Integer id) {
        Patient found = getPatientById(id);
        if (found != null)
            patientRepo.delete(found);
        else
            throw new CustomException("Patient does not exist", HttpStatus.BAD_REQUEST);
    }
}
