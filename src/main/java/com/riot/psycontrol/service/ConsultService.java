package com.riot.psycontrol.service;

import com.riot.psycontrol.dao.Consult;
import com.riot.psycontrol.dao.Patient;
import com.riot.psycontrol.repo.ConsultRepo;
import com.riot.psycontrol.repo.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultService {

    @Autowired
    private ConsultRepo consultRepo;

    @Autowired
    private PatientRepo patientRepo;

    public List<Consult> getConsultsByPatientId(Integer id){
        return consultRepo.getConsultsByPatientId(id);
    }

    public Consult getConsult(Integer id){
        return consultRepo.findById(id).get();
    }

    public Consult saveConsult(Integer patientId, Consult consult){
        Patient currentPatient = patientRepo.findById(patientId).get();
        Consult nuevo = new Consult();
        nuevo.setReason(consult.getReason());
        nuevo.setDescription(consult.getDescription());
        nuevo.setPatient(currentPatient);
        return consultRepo.save(nuevo);
    }

    public void deleteConsult(Integer id){
        consultRepo.deleteById(id);
    }

    public Consult updateConsult(Consult consult) {
        Consult updated = consultRepo.getConsultById(consult.getId());
        updated.setReason(consult.getReason());
        updated.setDescription(consult.getDescription());
        return updated;
    }
}
