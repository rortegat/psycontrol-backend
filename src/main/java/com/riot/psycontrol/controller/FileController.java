package com.riot.psycontrol.controller;

import com.riot.psycontrol.dao.DaoFile;
import com.riot.psycontrol.service.DaoFileService;
import com.riot.psycontrol.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${config.file.upload-dir}")
    public String rootDir;

    @Autowired
    DaoFileService daoFileService;

    @Autowired
    UserService userService;

    @PostMapping("/upload")
    public DaoFile uploadUserFile( @RequestParam("file") MultipartFile file) {
        String dirPath = rootDir + File.separator+userService.whoAmI().getUsername();
        return daoFileService.uploadFile(dirPath, file);
    }

    @GetMapping("/all")
    public List<DaoFile> getAllUserFiles() {
        return daoFileService.getAllUserFiles(userService.whoAmI().getUsername());
    }

    @GetMapping("/preview/{id}")
    public ResponseEntity<Resource> getImageAsset(@PathVariable String id) {
        DaoFile file = daoFileService.getById(id);
        Resource resource = daoFileService.getResource(file.getPath(), file.getFilename());
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "filename=\"" + file.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteFile( @PathVariable String id) {
        daoFileService.deleteFile(id);
    }
}
