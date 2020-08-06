package com.riot.psycontrol.dto;

import lombok.Data;

@Data
public class FileDTO {

    private String id;
    private String filename;
    private String url;
    private String type;
    private long size;

}
