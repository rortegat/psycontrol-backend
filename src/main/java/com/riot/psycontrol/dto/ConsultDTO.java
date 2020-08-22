package com.riot.psycontrol.dto;

import com.riot.psycontrol.entity.Consult;
import com.riot.psycontrol.entity.Patient;
import lombok.Data;

import java.util.Date;

@Data
public class ConsultDTO {
    private Integer id;
    private String reason;
    private String description;
    private Date createdDate;
    private Integer patientId;

    public ConsultDTO(Consult consult){
        this.id=consult.getId();
        this.reason=consult.getReason();
        this.description=consult.getDescription();
        this.createdDate=consult.getCreatedDate();
        this.patientId=consult.getPatient().getId();
    }
}
