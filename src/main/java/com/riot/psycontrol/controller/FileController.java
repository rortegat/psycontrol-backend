package com.riot.psycontrol.controller;

import com.riot.psycontrol.dto.FileDTO;
import com.riot.psycontrol.service.impl.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${config.file.upload-dir}")
    private String rootDir;

    @Autowired
    private FileServiceImpl fileServiceImpl;

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload")
    public FileDTO uploadUserFile(@RequestParam("file") MultipartFile file, Principal principal) {
        String dirPath = rootDir + File.separator+principal.getName();
        return fileServiceImpl.uploadFile(dirPath, file);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public List<FileDTO> getAllUserFiles(Principal principal){
        return fileServiceImpl.getAllUserFiles(principal.getName());
    }

    @GetMapping("/preview/{id}")
    public ResponseEntity<Resource> getImageAsset(@PathVariable String id) {
        var file = fileServiceImpl.getById(id);
        Resource resource = fileServiceImpl.getResource(file.getPath(), file.getFilename());
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "filename=\"" + file.getFilename() + "\"")
                .body(resource);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public void deleteFile( @PathVariable String id) {
        fileServiceImpl.deleteFile(id);
    }
}
