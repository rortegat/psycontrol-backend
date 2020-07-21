package com.riot.psycontrol.service;

import com.riot.psycontrol.dao.Privilege;
import com.riot.psycontrol.dao.Role;
import com.riot.psycontrol.repo.RoleRepo;
import com.riot.psycontrol.security.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepo roleRepo;

    public Role getRole(String rolename) {
        return roleRepo.findByRolename(rolename);
    }

    public Role getRoleById(Integer id) {
        return roleRepo.findById(id).get();
    }

    public List<Role> getRoles() {
        return roleRepo.findAll();
    }

    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }

    public Role updateRole(Role role) {
        Role found = getRoleById(role.getId());
        if (found != null) {
            found.setRolename(role.getRolename());
            found.setPrivileges(role.getPrivileges());
            return roleRepo.save(found);
        } else
            throw new CustomException("Role does not exist", HttpStatus.BAD_REQUEST);
    }

    public void deleteRole(Integer id) {
        Role found = getRoleById(id);
        if (found != null)
            roleRepo.delete(found);
        else
            throw new CustomException("Role does not exist", HttpStatus.BAD_REQUEST);
    }
}
