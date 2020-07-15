package com.riot.psycontrol.service;

import com.riot.psycontrol.dao.DaoFile;
import com.riot.psycontrol.repo.DaoFileRepo;
import com.riot.psycontrol.security.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class DaoFileService {

    @Autowired
    private DaoFileRepo DaoFileRepo;

    public List<DaoFile> getAllUserFiles(String username) {
        return DaoFileRepo.findByCreatedBy(username);
    }


    public DaoFile getById(String id) {
        Optional<DaoFile> file = DaoFileRepo.findById(id);
        if (file.isPresent())
            return file.get();
        else throw new CustomException("This file doesn't exist", HttpStatus.BAD_REQUEST);
    }

    public DaoFile uploadFile(String path, MultipartFile file) {
        File folder = new File(path);

        if (!folder.isDirectory()) {
            try {
                Files.createDirectory(folder.toPath()).toFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (String ele : folder.list()) {
            if (ele.equals(file.getOriginalFilename()))
                throw new CustomException("This file already exist", HttpStatus.NOT_MODIFIED);
        }


        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path filePath = Paths.get(path);

        try {
            if (file.isEmpty()) {
                throw new CustomException("Failed to store empty file " + filename, HttpStatus.NOT_MODIFIED);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new CustomException("Cannot store file with relative path outside current directory " + filename, HttpStatus.NOT_MODIFIED);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream,
                        filePath.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
                DaoFile nuevo = new DaoFile(
                        UUID.randomUUID().toString(),
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getSize(),
                        filePath.toString());
                return DaoFileRepo.save(nuevo);
            }
        } catch (IOException e) {
            throw new CustomException("Failed to store file " + filename, HttpStatus.NOT_MODIFIED);
        }
    }

    public void deleteFile(String id) {
        Optional<DaoFile> file = DaoFileRepo.findById(id);
        if (file.isPresent()) {
            if (getFile(file.get().getPath(), file.get().getFilename()).delete())
                DaoFileRepo.delete(file.get());
            else throw new CustomException("Could not delete file", HttpStatus.NOT_MODIFIED);
        } else {
            throw new CustomException("This file does not exist", HttpStatus.BAD_REQUEST);
        }
    }

    //Retrieves file from directory
    public File getFile(String path, String filename) {
        return new File(path + File.separator + filename);
    }

    public Path getPath(String filePath, String filename) {
        Path rootLocation = Paths.get(filePath);
        return rootLocation.resolve(filename);
    }

    public Stream<Path> getDirectoryFiles(String directoryPath) {
        Path rootLocation = Paths.get(directoryPath);
        try {
            return Files.walk(rootLocation, 1)
                    .filter(path -> !path.equals(rootLocation))
                    .map(rootLocation::relativize);
        } catch (IOException e) {
            throw new CustomException("Failed to read stored files", HttpStatus.BAD_REQUEST);
        }
    }

    public Resource getResource(String filePath, String filename) {
        try {
            Path path = getPath(filePath, filename);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new CustomException("Could not read file: " + filename, HttpStatus.BAD_REQUEST);
            }
        } catch (MalformedURLException e) {
            throw new CustomException("Could not read file: " + filename, HttpStatus.BAD_REQUEST);
        }
    }

}
