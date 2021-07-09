package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Image {
    private Integer imageId;
    private String imageName;
    private  Long size;
    private  String uploadTime;
    private  String md5;//文件唯一性MD5校验码
    private String contentType;
    private String path;
}
