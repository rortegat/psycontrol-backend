package com.riot.psycontrol.model;

import lombok.Data;

import javax.persistence.Column;

@Data
public class FileResponse {

    private String filename;
    private String fileUri;
    private String type;
    private long size;

}
