package com.riot.psycontrol.controller;

import com.riot.psycontrol.dao.Role;
import com.riot.psycontrol.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping("/all")
    public List<Role> getRoles(){
        return roleService.getRoles();
    }

    @PostMapping("/add")
    public Role addRole(@RequestBody Role role){
        return roleService.saveRole(role);
    }

}
