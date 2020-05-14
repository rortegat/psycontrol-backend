package com.riot.psycontrol.service;

import com.riot.psycontrol.dao.Patient;
import com.riot.psycontrol.repo.PatientRepo;
import com.riot.psycontrol.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired PatientRepo patientRepo;

    @Autowired
    UserService userService;

    public List<Patient> getPatients(){
        return patientRepo.getPatientsByUserId(userService.whoAmI().getId());
    }

    public Patient getPatientById(Integer id){
        return patientRepo.findById(id).get();
    }

    public Patient savePatient(Patient patient){
        String username=((UserDetails)SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();
        patient.setUser(userService.whoAmI());
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

    public void removePatient(Integer id){
        patientRepo.deleteById(id);
    }
}
