package com.riot.psycontrol.repo;

import com.riot.psycontrol.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,String> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
    void deleteByUsername(String username);
}
