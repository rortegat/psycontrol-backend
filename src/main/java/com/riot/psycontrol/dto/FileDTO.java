package com.riot.psycontrol.dto;

import com.riot.psycontrol.entity.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO implements Serializable {
    private String id;
    private String filename;
    private String url;
    private String type;
    private long size;

    public FileDTO(FileEntity fileEntity) {
        this.id = fileEntity.getId();
        this.filename = fileEntity.getFilename();
        this.url = "";
        this.type = fileEntity.getContentType();
        this.size = fileEntity.getSize();
    }
}
