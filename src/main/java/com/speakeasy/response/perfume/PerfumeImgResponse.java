package com.speakeasy.response.perfume;

import com.speakeasy.domain.perfume.PerfumeImages;
import lombok.Getter;

@Getter
public class PerfumeImgResponse {

    private String newFileName;
//    private final String path = "src/main/resources/static/img/";

    public PerfumeImgResponse(PerfumeImages perfumeImages){
        this.newFileName = perfumeImages.getNewFileName();
    }


}
