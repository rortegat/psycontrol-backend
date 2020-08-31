package com.riot.psycontrol.service.impl;

import com.riot.psycontrol.dto.PrivilegeDTO;
import com.riot.psycontrol.repo.PrivilegeRepo;
import com.riot.psycontrol.service.IPrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("privilegeServiceImpl")
public class PrivilegeServiceImpl implements IPrivilegeService {

    @Autowired
    PrivilegeRepo privilegeRepo;

    @Override
    public List<PrivilegeDTO> getPrivileges() {
        return privilegeRepo.findAll()
                .stream()
                .map(PrivilegeDTO::new)
                .collect(Collectors.toList());
    }

}
