package com.paw.trello.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TablesDto {

    private long id;
    private String user;
    private String tableName;
    private String picName;
    private String mimetype;
    private byte[] pic;
}

