package com.speakeasy.response;

import lombok.Builder;
import lombok.Getter;

import java.io.File;
import java.util.List;

@Getter
public class ItemImgResponse {

    private Long id;
    private List<String> images;
    private final String path = "src/main/resources/static/img/";

    @Builder
    public ItemImgResponse(Long id) {
        this.id = id;
    }

    public File[] getImg(Long id){
        File dir = new File(path+id);
        File[] image = dir.listFiles();
        return image;
    }
}
