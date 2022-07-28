package com.speakeasy.response;

import com.speakeasy.domain.Item;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;


@Getter
public class ItemDetailResponse {

    private final Long id;
    private final String name;
    private final String note;
    private final String incense;
    private final String season;
    private final String base;
    private List<ItemImgResponse> images;
    private List<ItemCommentResponse> comments;



    public ItemDetailResponse(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.note = item.getNote();
        this.incense = item.getIncense();
        this.season = item.getSeason();
        this.base = item.getBase();
        this.comments = item.getComments().stream()
                .map(ItemCommentResponse::new)
                .collect(Collectors.toList());
        this.images = item.getImages().stream()
                .map(ItemImgResponse::new)
                .collect(Collectors.toList());
    }
}
