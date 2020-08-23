package com.riot.psycontrol.dto;

import com.riot.psycontrol.entity.Privilege;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeDTO implements Serializable {
    private Integer id;
    private String privilegename;

    public PrivilegeDTO(Privilege privilege) {
        this.id = privilege.getId();
        this.privilegename = privilege.getPrivilegename();
    }
}
