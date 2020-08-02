package com.riot.psycontrol.controller;

import com.riot.psycontrol.entity.Role;
import com.riot.psycontrol.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping("/{id}")
    public Role getRole(@PathVariable Integer id) {
        return roleService.getRoleById(id);
    }

    @GetMapping("/all")
    public List<Role> getRoles() {
        return roleService.getRoles();
    }

    @PostMapping("/add")
    public Role addRole(@RequestBody Role role) {
        return roleService.saveRole(role);
    }

    @PutMapping("/update")
    public Role updateRole(@RequestBody Role role) {
        return roleService.updateRole(role);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
    }

}
