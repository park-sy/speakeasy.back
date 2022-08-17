package com.speakeasy.response;

import com.speakeasy.domain.Item;
import com.speakeasy.domain.Note;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
public class ItemDetailResponse {

    private final Long id;
    private final String name;
    private final String brand;
    private final Integer year;
    private final Integer gender;

    private final List<ItemNotesList> topNotes;
    private final List<ItemNotesList> heartNotes;
    private final List<ItemNotesList> baseNotes;

    private final double ratingPoints;
    private final double scentPoints;
    private final double longevityPoints;
    private final double sillagePoints;
    private final double bottlePoints;
    private final double valueOfMoneyPoints;
    private final Long votes;

    private final HashMap<String,Long> type;
    private final HashMap<String,Long> season;
    private final HashMap<String,Long> occasion;
    private final HashMap<String,Long> audience;
    private final String description;
    private final String perfumer;
    private List<ItemImgResponse> images;
    private List<ItemCommentResponse> comments;

    private final int view;

//    private List<ItemNotesList> notes;

    public ItemDetailResponse(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.brand = item.getBrand();
        this.year = item.getYear();
        this.gender = item.getGender();
        this.topNotes = item.getTopNotes().stream()
                .map(ItemNotesList::new)
                .collect(Collectors.toList());
        this.heartNotes = item.getHeartNotes().stream()
                .map(ItemNotesList::new)
                .collect(Collectors.toList());
        this.baseNotes = item.getBaseNotes().stream()
                .map(ItemNotesList::new)
                .collect(Collectors.toList());
        if(item.getVotes() == 0){
            this.ratingPoints = 0;
            this.scentPoints = 0;
            this.longevityPoints = 0;
            this.sillagePoints = 0;
            this.bottlePoints = 0;
            this.valueOfMoneyPoints = 0;
        }else{
            this.ratingPoints = Math.round(item.getRatingPoints()/(double)item.getVotes()*10)/10.0;
            this.scentPoints = Math.round(item.getScentPoints()/(double)item.getVotes()*10)/10.0;
            this.longevityPoints = Math.round(item.getLongevityPoints()/(double)item.getVotes()*10)/10.0;
            this.sillagePoints = Math.round(item.getSillagePoints()/(double)item.getVotes()*10)/10.0;
            this.bottlePoints = Math.round(item.getBottlePoints()/(double)item.getVotes()*10)/10.0;
            this.valueOfMoneyPoints = Math.round(item.getValueOfMoneyPoints()/(double) item.getVotes()*10)/10.0;
        }

        this.votes = item.getVotes();

        this.type = item.getType();
        this.season = item.getSeason();
        this.occasion = item.getOccasion();
        this.audience = item.getAudience();

        this.description = item.getDescription();
        this.perfumer = item.getPerfumer();
        this.comments = item.getComments().stream()
                .map(ItemCommentResponse::new)
                .collect(Collectors.toList());
        this.images = item.getImages().stream()
                .map(ItemImgResponse::new)
                .collect(Collectors.toList());
        this.view = item.getView();
    }
}
