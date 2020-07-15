package com.riot.psycontrol.model;

import com.riot.psycontrol.dao.Role;
import lombok.Data;

import java.util.List;

@Data
public class AuthenticationResponse {

    private String token;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private List<Role> roles;

}
