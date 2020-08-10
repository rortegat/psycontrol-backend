package com.riot.psycontrol.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ConsultDTO {
    private Integer id;
    private String reason;
    private String description;
    private Integer patientId;
    private Date createdDate;
    private Date lastModifiedDate;
}
