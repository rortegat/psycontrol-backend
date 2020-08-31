package com.riot.psycontrol.service.impl;

import com.riot.psycontrol.dto.ConsultDTO;
import com.riot.psycontrol.entity.Consult;
import com.riot.psycontrol.repo.ConsultRepo;
import com.riot.psycontrol.repo.PatientRepo;
import com.riot.psycontrol.service.IConsultService;
import com.riot.psycontrol.util.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service("consultServiceImpl")
public class ConsultServiceImpl implements IConsultService {

    @Autowired
    private ConsultRepo consultRepo;

    @Autowired
    private PatientRepo patientRepo;

    @Override
    public List<ConsultDTO> getConsultsByPatientId(@NotNull Integer id) {
        return consultRepo.findByPatientId(id)
                .stream()
                .map(ConsultDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public ConsultDTO getConsult(@NotNull Integer id) {
        var consult = consultRepo.findById(id);
        if (consult.isPresent())
            return new ConsultDTO(consult.get());
        else
            throw new CustomException("This consult does not exist", HttpStatus.BAD_REQUEST);

    }

    @Override
    public ConsultDTO saveConsult(@NotNull ConsultDTO consultDTO) {
        var consult = new Consult();
        var patient = patientRepo.findById(consultDTO.getPatientId());

        consult.setReason(consultDTO.getReason());
        consult.setDescription(consultDTO.getDescription());
        if (patient.isPresent())
            consult.setPatient(patient.get());
        else
            throw new CustomException("blablabla", HttpStatus.BAD_GATEWAY);
        return new ConsultDTO(consultRepo.save(consult));
    }

    @Override
    public ConsultDTO updateConsult(@NotNull ConsultDTO consultDTO) {
        var consult = consultRepo.findById(consultDTO.getId());
        if (consult.isPresent()) {
            var updated = consult.get();
            updated.setReason(consultDTO.getReason());
            updated.setDescription(consultDTO.getDescription());
            return new ConsultDTO(consultRepo.save(updated));
        } else
            throw new CustomException("This consult does not exist", HttpStatus.BAD_REQUEST);
    }

    @Override
    public void deleteConsult(@NotNull Integer id) {
        var consult = consultRepo.findById(id);
        if (consult.isPresent())
            consultRepo.delete(consult.get());
        else
            throw new CustomException("This consult does not exist", HttpStatus.BAD_REQUEST);

    }
}
