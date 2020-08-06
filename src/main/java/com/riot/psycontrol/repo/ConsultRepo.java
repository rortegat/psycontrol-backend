package com.riot.psycontrol.repo;

import com.riot.psycontrol.entity.Consult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultRepo extends JpaRepository<Consult, Integer> {
    List<Consult> findByPatientId(Integer id);
}
