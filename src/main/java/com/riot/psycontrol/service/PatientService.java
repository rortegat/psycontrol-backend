package com.riot.psycontrol.service;

import com.riot.psycontrol.dto.PatientDTO;
import com.riot.psycontrol.entity.Patient;
import com.riot.psycontrol.repo.PatientRepo;
import com.riot.psycontrol.security.CustomException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    ModelMapper modelMapper;

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

    public PatientDTO getPatientById(Integer id) {
        var patient = patientRepo.findById(id);
        if (patient.isPresent())
            return modelMapper.map(patient, PatientDTO.class);
        else
            throw new CustomException("This patient does not exist", HttpStatus.BAD_REQUEST);
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
        var found = patientRepo.findById(id);
        if (found.isPresent())
            patientRepo.delete(found.get());
        else
            throw new CustomException("Patient does not exist", HttpStatus.BAD_REQUEST);
    }
}
