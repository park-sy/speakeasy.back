package com.speakeasy.response;

import lombok.Builder;
import lombok.Getter;


@Getter
public class ItemDetailResponse {

    private final Long id;
    private final String name;
    private final String note;
    private final String incense;
    private final String season;
    private final String base;


    @Builder
    public ItemDetailResponse(Long id, String name, String note, String incense, String season, String base) {
        this.id = id;
        this.name = name;
        this.note = note;
        this.incense = incense;
        this.season = season;
        this.base = base;
    }
}
