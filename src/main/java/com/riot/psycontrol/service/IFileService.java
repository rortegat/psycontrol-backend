package com.riot.psycontrol.service;

import com.riot.psycontrol.dto.FileDTO;
import com.riot.psycontrol.entity.FileEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface IFileService {
    List<FileDTO> getAllUserFiles(String username);
    FileEntity getById(String id);
    FileDTO uploadFile(String path, MultipartFile file);
    void deleteFile(String id);
    File getFile(String path, String filename);
    Path getPath(String filePath, String filename);
    Stream<Path> getDirectoryFiles(String directoryPath);
    Resource getResource(String filePath, String filename);
}
