package com.riot.psycontrol.service.impl;

import com.riot.psycontrol.dto.RoleDTO;
import com.riot.psycontrol.entity.Role;
import com.riot.psycontrol.repo.PrivilegeRepo;
import com.riot.psycontrol.repo.RoleRepo;
import com.riot.psycontrol.service.IRoleService;
import com.riot.psycontrol.util.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service("roleServiceImpl")
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PrivilegeRepo privilegeRepo;

    @Override
    public List<RoleDTO> getRoles() {
        return roleRepo.findAll()
                .stream()
                .map(RoleDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO getRoleById(@NotNull Integer id) {
        var role = roleRepo.findById(id);
        if (role.isEmpty())
            throw new CustomException("This role does not exist", HttpStatus.BAD_REQUEST);
        return new RoleDTO(role.get());
    }

    @Override
    public RoleDTO saveRole(@NotNull RoleDTO roleDTO) {
        if (roleDTO.getPrivileges() == null || roleDTO.getPrivileges().isEmpty())
            throw new CustomException("You must declare at least one privilege to this role", HttpStatus.BAD_REQUEST);
        var role = new Role();
        role.setRolename(roleDTO.getRolename());
        role.setPrivileges(roleDTO.getPrivileges()
                .stream()
                .map(privilege -> privilegeRepo.findByPrivilegename(privilege))
                .collect(Collectors.toList()));
        return new RoleDTO(roleRepo.save(role));
    }

    @Override
    public RoleDTO updateRole(@NotNull RoleDTO roleDTO) {
        if (roleDTO.getPrivileges() == null || roleDTO.getPrivileges().isEmpty())
            throw new CustomException("You must declare at least one privilege to this role", HttpStatus.BAD_REQUEST);
        var role = roleRepo.findByRolename(roleDTO.getRolename());
        if (role != null) {
            role.setRolename(roleDTO.getRolename());
            role.setPrivileges(roleDTO.getPrivileges()
                    .stream()
                    .map(privilege -> privilegeRepo.findByPrivilegename(privilege))
                    .collect(Collectors.toList()));
            return new RoleDTO(roleRepo.save(role));
        } else
            throw new CustomException("This role does not exist", HttpStatus.BAD_REQUEST);
    }

    @Override
    public void deleteRole(@NotNull Integer id) {
        var role = roleRepo.findById(id);
        if (role.isPresent())
            roleRepo.delete(role.get());
        else
            throw new CustomException("This role does not exist", HttpStatus.BAD_REQUEST);
    }
}
