package com.riot.psycontrol.controller;

import com.riot.psycontrol.dto.RoleDTO;
import com.riot.psycontrol.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    @Qualifier("roleServiceImpl")
    IRoleService roleService;

    @GetMapping("/{id}")
    public RoleDTO getRole(@PathVariable Integer id) {
        return roleService.getRoleById(id);
    }

    @GetMapping("/all")
    public List<RoleDTO> getRoles() {
        return roleService.getRoles();
    }

    @PostMapping("/add")
    public RoleDTO addRole(@RequestBody RoleDTO role) {
        return roleService.saveRole(role);
    }

    @PutMapping("/update")
    public RoleDTO updateRole(@RequestBody RoleDTO role) {
        return roleService.updateRole(role);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
    }

}
