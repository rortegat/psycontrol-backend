package com.riot.psycontrol.service;

import com.riot.psycontrol.dto.PrivilegeDTO;

import java.util.List;

public interface IPrivilegeService {
    List<PrivilegeDTO> getPrivileges();
}
