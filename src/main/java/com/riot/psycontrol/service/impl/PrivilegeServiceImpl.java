package com.riot.psycontrol.service.impl;

import com.riot.psycontrol.dto.PrivilegeDTO;
import com.riot.psycontrol.entity.Privilege;
import com.riot.psycontrol.repo.PrivilegeRepo;
import com.riot.psycontrol.util.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrivilegeServiceImpl {

    @Autowired
    PrivilegeRepo privilegeRepo;

    public List<PrivilegeDTO> getPrivileges() {
        return privilegeRepo.findAll()
                .stream()
                .map(PrivilegeDTO::new)
                .collect(Collectors.toList());
    }

    public PrivilegeDTO getPrivilege(@NotNull String privilegename) {
        var privilege = privilegeRepo.findByPrivilegename(privilegename);
        if (privilege != null)
            return new PrivilegeDTO(privilege);
        else
            throw new CustomException("This privilege does not exist", HttpStatus.BAD_REQUEST);
    }

    public PrivilegeDTO createPrivilege(@NotNull PrivilegeDTO privilegeDTO) {
        var privilege = new Privilege();
        privilege.setPrivilegename(privilegeDTO.getPrivilegename());
        return new PrivilegeDTO(privilegeRepo.save(privilege));
    }

    public PrivilegeDTO updatePrivilege(@NotNull PrivilegeDTO privilegeDTO) {
        var privilege = privilegeRepo.findByPrivilegename(privilegeDTO.getPrivilegename());
        if (privilege != null) {
            privilege.setPrivilegename(privilege.getPrivilegename());
            return new PrivilegeDTO(privilegeRepo.save(privilege));
        } else
            throw new CustomException("Privilege does not exist", HttpStatus.BAD_REQUEST);
    }

}
