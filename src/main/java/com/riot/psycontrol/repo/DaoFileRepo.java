package com.riot.psycontrol.repo;

import com.riot.psycontrol.dao.DaoFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DaoFileRepo extends JpaRepository<DaoFile,Integer> {
}
