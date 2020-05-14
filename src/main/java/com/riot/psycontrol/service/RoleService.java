package com.riot.psycontrol.service;

import com.riot.psycontrol.dao.Role;
import com.riot.psycontrol.repo.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepo roleRepo;

    public Role getRole(String rolename) {
        return roleRepo.findByRolename(rolename);
    }

    public List<Role> getRoles() {
        return roleRepo.findAll();
    }

    public Role saveRole(Role role){
        return roleRepo.save(role);
    }
}
