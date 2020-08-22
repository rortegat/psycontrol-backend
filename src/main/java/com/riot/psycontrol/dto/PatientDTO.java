package com.riot.psycontrol.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PatientDTO implements Serializable {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String mobile;
}
