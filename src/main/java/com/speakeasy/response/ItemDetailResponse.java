package com.speakeasy.response;

import com.speakeasy.domain.Item;
import com.speakeasy.domain.Note;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
public class ItemDetailResponse {

    private final Long id;
    private final String name;
    private final String brand;
    private final String url;
    private final Set<Long> topNotes;
    private final Set<Long> heartNotes;
    private final Set<Long> baseNotes;

    private final double ratingPoints;
    private final Long ratingVotes;
    private final double scentPoints;
    private final Long scentVotes;
    private final double longevityPoints;
    private final Long longevityVotes;
    private final double sillagePoints;
    private final Long sillageVotes;
    private final double bottlePoints;
    private final Long bottleVotes;
    private final double valueOfMoneyPoints;
    private final Long valueOfMoneyVotes;

    private final String type;
    private final String season;
    private final String occasion;
    private final String audience;
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
        this.url = item.getUrl();
        this.topNotes = item.getTopNotes();
        this.heartNotes = item.getHeartNotes();
        this.baseNotes = item.getBaseNotes();

        this.ratingPoints = item.getRatingPoints()/(double)item.getRatingVotes();
        this.ratingVotes = item.getRatingVotes();
        this.scentPoints =item.getScentPoints()/(double)item.getScentVotes();
        this.scentVotes = item.getScentVotes();
        this.longevityPoints = item.getLongevityPoints()/(double)item.getLongevityVotes();
        this.longevityVotes = item.getLongevityVotes();
        this.sillagePoints = item.getSillagePoints()/(double)item.getSillageVotes();
        this.sillageVotes = item.getSillageVotes();
        this.bottlePoints = item.getBottlePoints()/(double)item.getBottleVotes();
        this.bottleVotes = item.getBottleVotes();
        this.valueOfMoneyPoints = item.getValueOfMoneyPoints()/(double) item.getValueOfMoneyVotes();
        this.valueOfMoneyVotes = item.getValueOfMoneyVotes();

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

//        this.notes = item.getTmpNotes().stream()
//                .map(ItemNotesList::new)
//                .collect(Collectors.toList());
    }
}
