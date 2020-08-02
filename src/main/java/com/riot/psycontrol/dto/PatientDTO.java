package com.riot.psycontrol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO implements Serializable {

    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String mobile;
}
