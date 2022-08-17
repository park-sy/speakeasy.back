package com.speakeasy.response.perfume;

import com.speakeasy.domain.perfume.Perfume;
import lombok.Getter;

//응답 처리를 위한 클래스 생성
@Getter
public class PerfumeResponse {

    private final Long id;
    private final String name;
    private final String brand;


//    @Builder //빌더 패턴 새용
//    public PerfumeResponse(Long id, String name, String topNotes, String brand, String season) {
//        this.id = id;
//        this.name = name;
//        this.topNotes = topNotes;
////        this.brand = brand;
////        this.season = season;
//        this.mainImg = path+name+"main.jpg";
//    }

    public PerfumeResponse(Perfume perfume) {
        this.id = perfume.getId();
        this.name = perfume.getName();
        this.brand = perfume.getBrand();
    }

}
