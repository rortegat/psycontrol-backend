package com.riot.psycontrol.dto;

import com.riot.psycontrol.entity.Privilege;
import com.riot.psycontrol.entity.Role;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class RoleDTO {
    private String rolename;
    private Set<String> privileges;

    public RoleDTO(Role role) {
        this.rolename = role.getRolename();
        this.privileges = role.getPrivileges()
                .stream()
                .map(Privilege::getPrivilegename)
                .collect(Collectors.toSet());
    }
}
