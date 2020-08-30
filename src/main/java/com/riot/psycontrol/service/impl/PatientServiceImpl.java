package com.riot.psycontrol.service.impl;

import com.riot.psycontrol.dto.PatientDTO;
import com.riot.psycontrol.entity.Patient;
import com.riot.psycontrol.repo.PatientRepo;
import com.riot.psycontrol.repo.UserRepo;
import com.riot.psycontrol.service.IPatientService;
import com.riot.psycontrol.util.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements IPatientService {

    @Autowired
    PatientRepo patientRepo;

    @Autowired
    UserRepo userRepo;

    @Override
    public List<PatientDTO> getPatients(@NotNull String username) {
        var patients = patientRepo.findAllByCreatedBy(username);
        return patients.stream()
                .map(PatientDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PatientDTO> getPagePatients(@NotNull Pageable pageable, @NotNull String username) {
        var page = patientRepo.findAllByCreatedBy(pageable, username);
        return page.map(PatientDTO::new);
    }

    @Override
    public PatientDTO getPatientById(@NotNull Integer id) {
        var patient = patientRepo.findById(id);
        if (patient.isPresent())
            return new PatientDTO(patient.get());
        else
            throw new CustomException("This patient does not exist", HttpStatus.BAD_REQUEST);
    }

    @Override
    public PatientDTO savePatient(@NotNull PatientDTO patientDTO) {
        var patient = new Patient();
        patient.setFirstname(patientDTO.getFirstname());
        patient.setLastname(patientDTO.getLastname());
        patient.setEmail(patientDTO.getEmail());
        patient.setMobile(patientDTO.getMobile());
        patient.setPhone(patientDTO.getPhone());
        patient.setUser(userRepo.findByUsername(patientDTO.getUsername()));
        return new PatientDTO(patientRepo.save(patient));
    }

    @Override
    public PatientDTO updatePatient(@NotNull PatientDTO patientDTO) {
        var patient = patientRepo.findById(patientDTO.getId());
        if (patient.isPresent()) {
            var updated = patient.get();
            updated.setFirstname(patientDTO.getFirstname());
            updated.setLastname(patientDTO.getLastname());
            updated.setEmail(patientDTO.getEmail());
            updated.setPhone(patientDTO.getPhone());
            updated.setMobile(patientDTO.getMobile());
            return new PatientDTO(patientRepo.save(updated));
        } else
            throw new CustomException("This patient does not exist", HttpStatus.BAD_REQUEST);
    }

    @Override
    public void removePatient(@NotNull Integer id) {
        var found = patientRepo.findById(id);
        if (found.isPresent())
            patientRepo.delete(found.get());
        else
            throw new CustomException("Patient does not exist", HttpStatus.BAD_REQUEST);
    }
}
