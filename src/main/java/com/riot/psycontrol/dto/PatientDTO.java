package com.riot.psycontrol.dto;

import com.riot.psycontrol.entity.Patient;
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
    private String username;

    public PatientDTO(Patient patient) {
        this.id = patient.getId();
        this.firstname = patient.getFirstname();
        this.lastname = patient.getLastname();
        this.email = patient.getMobile();
        this.mobile = patient.getMobile();
        this.username = patient.getUser().getUsername();
    }
}
