package com.riot.psycontrol.dto;

import com.riot.psycontrol.entity.Privilege;
import com.riot.psycontrol.entity.Role;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Integer id;
    private String rolename;
    private Set<String> privileges;

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.rolename = role.getRolename();
        this.privileges = role.getPrivileges()
                .stream()
                .map(Privilege::getPrivilegename)
                .collect(Collectors.toSet());
    }
}
