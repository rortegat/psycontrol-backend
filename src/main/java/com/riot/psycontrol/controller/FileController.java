package com.riot.psycontrol.controller;

import com.riot.psycontrol.dao.DaoFile;
import com.riot.psycontrol.model.FileResponse;
import com.riot.psycontrol.security.CustomException;
import com.riot.psycontrol.service.DaoFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${config.file.upload-dir}")
    public String rootDir;

    @Autowired
    DaoFileService daoFileService;

    @PostMapping("/upload")
    public FileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        return daoFileService.uploadFile(file);
    }

    @PostMapping("/{directory}/images/upload")
    public DaoFile uploadAssetImage(
            @PathVariable String directory,
            @RequestParam("file") MultipartFile file) {
        String dirPath = rootDir + File.separator + directory;
        return daoFileService.uploadImageFile(dirPath, file);
    }

    @GetMapping("/{directory}/images/list")
    public List<String> getAllImages(@PathVariable String directory) {
        String dirPath = rootDir + File.separator + directory;
        List<String> files = new ArrayList<>();
        daoFileService.getDirectoryFiles(dirPath).forEach((path) -> {
            files.add(path.getFileName().toString());
        });
        return files;
    }

    @GetMapping("/{directory}/images/{filename:.+}")
    public ResponseEntity<Resource> getImageAsset(
            @PathVariable String directory,
            @PathVariable String filename) {
        String dirPath = rootDir + File.separator + directory;
        //File file =  dbFileService.getFile(path, filename);
        String mimeType = URLConnection.guessContentTypeFromName(filename);
        if (mimeType == null) {
            throw new CustomException("This file extension is not supported", HttpStatus.CONFLICT);
        }
        Resource resource = daoFileService.getResource(dirPath, filename);

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "filename=\"" + filename + "\"")
                .body(resource);
    }

    @DeleteMapping("/{directory}/images/{filename:.+}")
    public void deleteFile(@PathVariable String directory,
                           @PathVariable String filename) {
        String dirPath = rootDir + File.separator + directory;
        daoFileService.deleteFile(dirPath, filename);
    }
}
