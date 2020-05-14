package com.riot.psycontrol.repo;

import com.riot.psycontrol.dao.Consult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultRepo extends JpaRepository<Consult, Integer> {

    @Query(value = "SELECT * FROM consult WHERE patient_id = :id",
    nativeQuery = true)

    List<Consult> getConsultsByPatientId(@Param("id") Integer id);

    Consult getConsultById(Integer id);

}
