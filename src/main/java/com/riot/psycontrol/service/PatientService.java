package com.riot.psycontrol.service;

import com.riot.psycontrol.dto.PatientDTO;
import com.riot.psycontrol.entity.Patient;
import com.riot.psycontrol.repo.PatientRepo;
import com.riot.psycontrol.util.CustomException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PatientRepo patientRepo;

    public List<PatientDTO> getPatients(@NotNull String username) {
        var patients = patientRepo.findAllByCreatedBy(username);
        return patients.stream()
                .map(patient -> modelMapper.map(patient, PatientDTO.class))
                .collect(Collectors.toList());
    }

    public Page<PatientDTO> getPagePatients(@NotNull Pageable pageable, @NotNull String username) {
        var page = patientRepo.findAllByCreatedBy(pageable, username);
        return page.map(patient -> modelMapper.map(patient, PatientDTO.class));
    }

    public PatientDTO getPatientById(@NotNull Integer id) {
        var patient = patientRepo.findById(id);
        if (patient.isPresent())
            return modelMapper.map(patient.get(), PatientDTO.class);
        else  
            throw new CustomException("This patient does not exist", HttpStatus.BAD_REQUEST);
    }

    public PatientDTO savePatient(@NotNull PatientDTO patientDTO) {
        var patient = modelMapper.map(patientDTO, Patient.class);
        return modelMapper.map(patientRepo.save(patient), PatientDTO.class);
    }

    public PatientDTO updatePatient(@NotNull PatientDTO patientDTO) {
        var patient = patientRepo.findById(patientDTO.getId());
        if (patient.isPresent()) {
            var updated = patient.get();
            updated.setFirstname(patientDTO.getFirstname());
            updated.setLastname(patientDTO.getLastname());
            updated.setEmail(patientDTO.getEmail());
            updated.setPhone(patientDTO.getPhone());
            updated.setMobile(patientDTO.getMobile());
            return modelMapper.map(patientRepo.save(updated), PatientDTO.class);
        } else
            throw new CustomException("This patient does not exist", HttpStatus.BAD_REQUEST);
    }

    public void removePatient(@NotNull Integer id) {
        var found = patientRepo.findById(id);
        if (found.isPresent())
            patientRepo.delete(found.get());
        else
            throw new CustomException("Patient does not exist", HttpStatus.BAD_REQUEST);
    }
}
