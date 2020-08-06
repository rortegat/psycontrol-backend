package com.riot.psycontrol.dto;

import lombok.Data;

@Data
public class ConsultDTO {
    private Integer id;
    private String reason;
    private String description;
    private Integer patientId;
}
