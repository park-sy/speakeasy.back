package com.speakeasy.response;

import com.speakeasy.domain.Item;
import lombok.Builder;
import lombok.Getter;

//응답 처리를 위한 클래스 생성
@Getter
public class ItemResponse {
    private final String path = "/resources/static/img/";
    private final Long id;
    private final String name;
    private final String note;
    private final String incense;
    private final String season;
    private final String mainImg;


    @Builder //빌더 패턴 새용
    public ItemResponse(Long id, String name, String note, String incense, String season) {
        this.id = id;
        this.name = name;
        this.note = note;
        this.incense = incense;
        this.season = season;
        this.mainImg = path+name+"main.jpg";
    }

    public ItemResponse(ItemResponse itemResponse) {
        this.id = itemResponse.getId();
        this.name = itemResponse.getName();
        this.note = itemResponse.getNote();
        this.incense = itemResponse.getIncense();
        this.season = itemResponse.getSeason();
        this.mainImg = path+itemResponse.getName()+"main.jpg";
    }
}
