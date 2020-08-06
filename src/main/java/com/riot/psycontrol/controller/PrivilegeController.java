package com.riot.psycontrol.controller;

import com.riot.psycontrol.dto.PrivilegeDTO;
import com.riot.psycontrol.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/privileges")
public class PrivilegeController {

    @Autowired
    PrivilegeService privilegeService;

    @GetMapping("/all")
    public List<PrivilegeDTO> getPrivilegesList(){
        return  privilegeService.getPrivileges();
    }

}
