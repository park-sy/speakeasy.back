package com.speakeasy.response;

import com.querydsl.core.Tuple;
import com.speakeasy.domain.Item;
import lombok.Getter;

//응답 처리를 위한 클래스 생성
@Getter
public class ItemResponse {

    private final Long id;
    private final String name;
    private final String brand;


//    @Builder //빌더 패턴 새용
//    public ItemResponse(Long id, String name, String topNotes, String brand, String season) {
//        this.id = id;
//        this.name = name;
//        this.topNotes = topNotes;
////        this.brand = brand;
////        this.season = season;
//        this.mainImg = path+name+"main.jpg";
//    }

    public ItemResponse(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.brand = item.getBrand();
    }

}
