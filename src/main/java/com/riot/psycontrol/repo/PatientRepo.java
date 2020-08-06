package com.riot.psycontrol.repo;

import com.riot.psycontrol.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepo extends JpaRepository<Patient, Integer> {
    Page<Patient> findAllByCreatedBy(Pageable pageable, String username);
    List<Patient> findAllByCreatedBy(String username);
}
