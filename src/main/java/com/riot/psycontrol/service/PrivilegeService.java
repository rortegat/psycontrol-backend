package com.riot.psycontrol.service;

import com.riot.psycontrol.dao.Privilege;
import com.riot.psycontrol.repo.PrivilegeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrivilegeService {
    @Autowired
    PrivilegeRepo privilegeRepo;

    public List<Privilege> getPrivileges(){
        return  privilegeRepo.findAll();
    }

    public Privilege createPrivilege(Privilege privilege){
        return privilegeRepo.save(privilege);
    }

    public void deletePrivilege(Privilege privilege){
        privilegeRepo.delete(privilege);
    }
}
