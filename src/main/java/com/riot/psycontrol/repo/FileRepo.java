package com.riot.psycontrol.repo;

import com.riot.psycontrol.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepo extends JpaRepository<FileEntity, String> {
    List<FileEntity> findByCreatedBy(String username);

    @Query("Select f from FileEntity f Where f.path Like '%folder'")
    List<FileEntity> findAllFolderDocuments();

    boolean existsById(String id);

    boolean existsByFilename(String filename);
}
