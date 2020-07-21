package com.riot.psycontrol.service;

import com.riot.psycontrol.dao.Privilege;
import com.riot.psycontrol.repo.PrivilegeRepo;
import com.riot.psycontrol.security.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrivilegeService {
    @Autowired
    PrivilegeRepo privilegeRepo;

    public List<Privilege> getPrivileges() {
        return privilegeRepo.findAll();
    }

    public Privilege getPrivilegeById(Integer id) {
        return privilegeRepo.findById(id).get();
    }

    public Privilege createPrivilege(Privilege privilege) {
        return privilegeRepo.save(privilege);
    }

    public void deletePrivilege(Privilege privilege) {
        privilegeRepo.delete(privilege);
    }

    public Privilege updatePrivilege(Privilege privilege) {
        Privilege found = getPrivilegeById(privilege.getId());
        if (found != null) {
            found.setPrivilegename(privilege.getPrivilegename());
            return privilegeRepo.save(found);
        } else
            throw new CustomException("Privilege does not exist", HttpStatus.BAD_REQUEST);
    }
}
