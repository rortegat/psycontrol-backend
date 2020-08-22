package com.riot.psycontrol.dto;

import com.riot.psycontrol.entity.Role;
import com.riot.psycontrol.entity.User;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private Set<String> roles;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.roles = user.getRoles()
                .stream()
                .map(Role::getRolename)
                .collect(Collectors.toSet());
    }
}
