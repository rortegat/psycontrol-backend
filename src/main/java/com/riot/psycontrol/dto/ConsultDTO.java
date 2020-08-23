package com.riot.psycontrol.dto;

import com.riot.psycontrol.entity.Consult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultDTO implements Serializable {
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
