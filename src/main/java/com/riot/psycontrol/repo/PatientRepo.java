package com.riot.psycontrol.repo;

import com.riot.psycontrol.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepo extends JpaRepository<Patient, Integer> {
    List<Patient> findAllByCreatedBy(String username);
}
