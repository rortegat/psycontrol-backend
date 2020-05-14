package com.riot.psycontrol.service;

import com.riot.psycontrol.dao.DaoFile;
import com.riot.psycontrol.model.FileResponse;
import com.riot.psycontrol.repo.DaoFileRepo;
import com.riot.psycontrol.security.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class DaoFileService {

    @Autowired
    private DaoFileRepo daoFileRepo;

    @Autowired
    private UserService userService;

    @Value("${config.file.upload-dir}")
    public String uploadDir;

    public FileResponse uploadFile(MultipartFile file) {

        FileResponse nuevo = new FileResponse();

        String userDir = File.separator + userService.whoAmI().getUsername();

        File folder = new File(uploadDir +  userDir);

        if (!folder.isDirectory()) {
            try {
                Files.createDirectory(folder.toPath()).toFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path rootLocation = Paths.get(uploadDir+userDir);

        try {
            if (file.isEmpty()) {
                throw new CustomException("Failed to store empty file " + filename, HttpStatus.NOT_MODIFIED);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new CustomException(
                        "Cannot store file with relative path outside current directory "+ filename,
                        HttpStatus.NOT_MODIFIED);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, rootLocation.resolve(filename),StandardCopyOption.REPLACE_EXISTING);
                //nuevo.setFilename(file.getOriginalFilename().substring(0,file.getOriginalFilename().indexOf(".")));
                nuevo.setFilename(file.getOriginalFilename());
                nuevo.setFileUri( ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(userDir)
                        .path("/download/")
                        .path(nuevo.getFilename())
                        .toUriString());
                nuevo.setSize(file.getSize());
                nuevo.setType(file.getContentType());
                /*DaoFile nuevo = new DaoFile();
                nuevo.setUser(userService.whoAmI());
                nuevo.setFilename(file.getOriginalFilename().substring(0,file.getOriginalFilename().indexOf(".")));
                nuevo.setSize(file.getSize());
                nuevo.setType(file.getContentType());
                fileRepo.save(nuevo);*/
            }
        }
        catch (IOException e) {
            throw new CustomException("Failed to store file " + filename, HttpStatus.NOT_MODIFIED);
        }
        return nuevo;
    }

    public Stream<Path> loadAll() {

        String userDir = File.separator + userService.whoAmI().getUsername();
        Path rootLocation = Paths.get(uploadDir+userDir);

        try {
            return Files.walk(rootLocation, 1)
                    .filter(path -> !path.equals(rootLocation))
                    .map(rootLocation::relativize);
        }
        catch (IOException e) {
            throw new CustomException("Failed to read stored files", HttpStatus.BAD_REQUEST);
        }

    }

    public Path load(String filename){
        String userDir = File.separator + userService.whoAmI().getUsername();
        Path rootLocation = Paths.get(uploadDir+userDir);
        return rootLocation.resolve(filename);
    }

    public Resource downloadFile(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new CustomException(
                        "Could not read file: "+ filename , HttpStatus.BAD_REQUEST);

            }
        }
        catch (MalformedURLException e) {
            throw new CustomException("Could not read file: " + filename, HttpStatus.BAD_REQUEST);
        }
    }

    public DaoFile uploadImageFile(String path, MultipartFile file) {

        DaoFile nuevo = new DaoFile();

        File folder = new File(path);

        if (!folder.isDirectory()) {
            try {
                Files.createDirectory(folder.toPath()).toFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for(String ele : folder.list()){
            if(ele.equals(file.getOriginalFilename()))
                throw new CustomException(
                        "This file already exists ",
                        HttpStatus.NOT_MODIFIED);
        }


        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path rootPath = Paths.get(path);

        try {
            if (file.isEmpty()) {
                throw new CustomException("Failed to store empty file " + filename, HttpStatus.NOT_MODIFIED);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new CustomException(
                        "Cannot store file with relative path outside current directory "+ filename,
                        HttpStatus.NOT_MODIFIED);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, rootPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
                nuevo.setFilename(file.getOriginalFilename());
                nuevo.setPath(rootPath.toString());
                /*nuevo.setUrl(
                        ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/files/preview/")
                        .path(nuevo.getFilename())
                        .toUriString());*/
                nuevo.setSize(file.getSize());
                nuevo.setContentType(file.getContentType());
                return nuevo;
            }
        }
        catch (IOException e) {
            throw new CustomException("Failed to store file " + filename, HttpStatus.NOT_MODIFIED);
        }
    }

    public File getFile(String path, String filename){
        return  new File(path+File.separator+filename);
    }

    public void deleteFile(String path, String filename){
        try{
            getFile(path, filename).delete();
        }catch ( Exception e){
            throw new CustomException(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }

    }

    public Path getPath(String filePath, String filename){
        Path rootLocation = Paths.get(filePath);
        return rootLocation.resolve(filename);
    }

    public Stream<Path> getDirectoryFiles(String directoryPath){
        Path rootLocation = Paths.get(directoryPath);
        try {
            return Files.walk(rootLocation, 1)
                    .filter(path -> !path.equals(rootLocation))
                    .map(rootLocation::relativize);
        }
        catch (IOException e) {
            throw new CustomException("Failed to read stored files", HttpStatus.BAD_REQUEST);
        }
    }

    public Resource getResource(String filePath, String filename) {
        try {
            Path path = getPath(filePath, filename);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new CustomException("Could not read file: "+ filename , HttpStatus.BAD_REQUEST);
            }
        }
        catch (MalformedURLException e) {
            throw new CustomException("Could not read file: " + filename, HttpStatus.BAD_REQUEST);
        }
    }

}
