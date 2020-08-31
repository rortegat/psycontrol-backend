package com.riot.psycontrol.service;

import com.riot.psycontrol.dto.RoleDTO;

import java.util.List;

public interface IRoleService {
    List<RoleDTO> getRoles();
    RoleDTO getRoleById(Integer id);
    RoleDTO saveRole(RoleDTO roleDTO);
    RoleDTO updateRole(RoleDTO roleDTO);
    void deleteRole(Integer id);
}
