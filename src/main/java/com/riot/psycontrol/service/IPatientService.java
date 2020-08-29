package com.riot.psycontrol.service;

import com.riot.psycontrol.dto.PatientDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IPatientService {
    List<PatientDTO> getPatients(String username);
    Page<PatientDTO> getPagePatients(Pageable pageable, String username);
    PatientDTO getPatientById(Integer id);
    PatientDTO savePatient(PatientDTO patientDTO);
    PatientDTO updatePatient(PatientDTO patientDTO);
    void removePatient(Integer id);
}
