package com.riot.psycontrol.service;

import com.riot.psycontrol.dto.PrivilegeDTO;
import com.riot.psycontrol.dto.RoleDTO;
import com.riot.psycontrol.entity.Role;
import com.riot.psycontrol.repo.PrivilegeRepo;
import com.riot.psycontrol.repo.RoleRepo;
import com.riot.psycontrol.security.CustomException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PrivilegeRepo privilegeRepo;

    @Autowired
    private ModelMapper modelMapper;

    public RoleDTO convertToDTO(@NotNull Role role) {
        var roleDTO = new RoleDTO();
        roleDTO.setRolename(role.getRolename());
        if (!role.getPrivileges().isEmpty())
            roleDTO.setPrivileges(
                    role.getPrivileges()
                            .stream()
                            .map(privilege -> modelMapper.map(privilege, PrivilegeDTO.class))
                            .collect(Collectors.toSet()));
        return roleDTO;
    }

    public RoleDTO getRoleById(@NotNull Integer id) {
        var role = roleRepo.findById(id);
        if (!role.isPresent())
            throw new CustomException("This role does not exist", HttpStatus.BAD_REQUEST);
        return convertToDTO(role.get());
    }

    public List<RoleDTO> getRoles() {
        return roleRepo.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RoleDTO saveRole(@NotNull RoleDTO roleDTO) {
        if(roleDTO.getPrivileges() == null || roleDTO.getPrivileges().isEmpty())
            throw new CustomException("You must declare at least one privilege to this role", HttpStatus.BAD_REQUEST);
        var role = new Role();
        role.setRolename(roleDTO.getRolename());
        role.setPrivileges(roleDTO.getPrivileges()
                .stream()
                .map(privilegeDTO -> privilegeRepo.findByPrivilegename(privilegeDTO.getPrivilegename()))
                .collect(Collectors.toList()));
        return convertToDTO(roleRepo.save(role));
    }

    public RoleDTO updateRole(@NotNull RoleDTO roleDTO) {
        if(roleDTO.getPrivileges() == null || roleDTO.getPrivileges().isEmpty())
            throw new CustomException("You must declare at least one privilege to this role", HttpStatus.BAD_REQUEST);
        var role = roleRepo.findByRolename(roleDTO.getRolename());
        if (role != null) {
            role.setRolename(roleDTO.getRolename());
            return convertToDTO(roleRepo.save(role));
        } else
            throw new CustomException("This role does not exist", HttpStatus.BAD_REQUEST);
    }

    public void deleteRole(@NotNull Integer id) {
        var role = roleRepo.findById(id);
        if (role.isPresent())
            roleRepo.delete(role.get());
        else
            throw new CustomException("This role does not exist", HttpStatus.BAD_REQUEST);
    }
}
