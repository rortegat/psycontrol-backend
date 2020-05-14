package com.riot.psycontrol.repo;

import com.riot.psycontrol.dao.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role,Integer> {
    Role findByRolename(String rolename);
}
