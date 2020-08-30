package com.riot.psycontrol.controller;

import com.riot.psycontrol.dto.RoleDTO;
import com.riot.psycontrol.service.impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    RoleServiceImpl roleServiceImpl;

    @GetMapping("/{id}")
    public RoleDTO getRole(@PathVariable Integer id) {
        return roleServiceImpl.getRoleById(id);
    }

    @GetMapping("/all")
    public List<RoleDTO> getRoles() {
        return roleServiceImpl.getRoles();
    }

    @PostMapping("/add")
    public RoleDTO addRole(@RequestBody RoleDTO role) {
        return roleServiceImpl.saveRole(role);
    }

    @PutMapping("/update")
    public RoleDTO updateRole(@RequestBody RoleDTO role) {
        return roleServiceImpl.updateRole(role);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRole(@PathVariable Integer id) {
        roleServiceImpl.deleteRole(id);
    }

}
