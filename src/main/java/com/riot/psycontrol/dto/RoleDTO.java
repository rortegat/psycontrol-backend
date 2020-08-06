package com.riot.psycontrol.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RoleDTO {
    private String rolename;
    private Set<PrivilegeDTO> privileges;
}
