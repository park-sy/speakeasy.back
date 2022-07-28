package com.speakeasy.response;

import com.speakeasy.domain.ItemImages;
import lombok.Builder;
import lombok.Getter;

import java.io.File;
import java.util.List;

@Getter
public class ItemImgResponse {

    private String newFileName;
//    private final String path = "src/main/resources/static/img/";

    public ItemImgResponse(ItemImages itemImages){
        this.newFileName = itemImages.getNewFileName();
    }


}
