package com.riot.psycontrol.service;

import com.riot.psycontrol.dto.ConsultDTO;
import com.riot.psycontrol.entity.Consult;
import com.riot.psycontrol.repo.ConsultRepo;
import com.riot.psycontrol.repo.PatientRepo;
import com.riot.psycontrol.security.CustomException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultService {

    @Autowired
    private ConsultRepo consultRepo;

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private ModelMapper modelMapper;

    public List<ConsultDTO> getConsultsByPatientId(@NotNull Integer id) {
        return consultRepo.findByPatientId(id)
                .stream()
                .map(consult -> modelMapper.map(consult, ConsultDTO.class))
                .collect(Collectors.toList());
    }

    public ConsultDTO getConsult(@NotNull Integer id) {
        var consult = consultRepo.findById(id);
        if (!consult.isPresent())
            throw new CustomException("This consult does not exist", HttpStatus.BAD_REQUEST);
        return modelMapper.map(consult.get(), ConsultDTO.class);
    }

    public ConsultDTO saveConsult(@NotNull ConsultDTO consultDTO) {
        var patient = patientRepo.findById(consultDTO.getPatientId());
        if (!patient.isPresent())
            throw new CustomException("This patient does not exist", HttpStatus.BAD_REQUEST);
        var consult = modelMapper.map(consultDTO, Consult.class);
        return modelMapper.map(consultRepo.save(consult), ConsultDTO.class);
    }

    public ConsultDTO updateConsult(@NotNull ConsultDTO consultDTO) {
        var consult = consultRepo.findById(consultDTO.getId());
        if (!consult.isPresent())
            throw new CustomException("This consult does not exist", HttpStatus.BAD_REQUEST);
        var updated = consult.get();
        updated.setReason(consultDTO.getReason());
        updated.setDescription(consultDTO.getDescription());
        return modelMapper.map(consultRepo.save(updated), ConsultDTO.class);
    }

    public void deleteConsult(@NotNull Integer id) {
        var consult = consultRepo.findById(id);
        if (!consult.isPresent())
            throw new CustomException("This consult does not exist", HttpStatus.BAD_REQUEST);
        consultRepo.delete(consult.get());
    }
}
