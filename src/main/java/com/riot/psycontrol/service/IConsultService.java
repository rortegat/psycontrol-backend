package com.riot.psycontrol.service;

import com.riot.psycontrol.dto.ConsultDTO;

import java.util.List;

public interface IConsultService {
    List<ConsultDTO> getConsultsByPatientId(Integer id);
    ConsultDTO getConsult(Integer id);
    ConsultDTO saveConsult(ConsultDTO consultDTO);
    ConsultDTO updateConsult(ConsultDTO consultDTO);
    void deleteConsult(Integer id);
}
