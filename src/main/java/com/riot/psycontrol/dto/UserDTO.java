package com.riot.psycontrol.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private Set<String> roles;
}
