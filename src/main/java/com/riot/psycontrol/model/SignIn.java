package com.riot.psycontrol.model;

import lombok.Data;

@Data
public class SignIn {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
}
