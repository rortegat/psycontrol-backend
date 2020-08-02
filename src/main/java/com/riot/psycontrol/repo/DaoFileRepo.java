package com.riot.psycontrol.repo;

import com.riot.psycontrol.entity.DaoFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DaoFileRepo extends JpaRepository<DaoFile, String> {
    List<DaoFile> findByCreatedBy(String username);

    @Query("Select f from DaoFile f Where f.path Like '%folder'")
    List<DaoFile> findAllFolderDocuments();

    boolean existsById(String id);

    boolean existsByFilename(String filename);
}
