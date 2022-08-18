package com.speakeasy.response.perfume;

import com.speakeasy.domain.perfume.Perfume;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Getter
public class PerfumeDetailResponse {

    private final Long id;
    private final String name;
    private final String brand;
    private final Integer year;
    private final Integer gender;

    private final List<PerfumeNotesList> topNotes;
    private final List<PerfumeNotesList> heartNotes;
    private final List<PerfumeNotesList> baseNotes;

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
    private List<PerfumeImgResponse> images;
    private List<PerfumeCommentResponse> comments;

    private final int view;

//    private List<PerfumeNotesList> notes;

    public PerfumeDetailResponse(Perfume perfume) {
        this.id = perfume.getId();
        this.name = perfume.getName();
        this.brand = perfume.getBrand();
        this.year = perfume.getYear();
        this.gender = perfume.getGender();
        this.topNotes = perfume.getTopNotes().stream()
                .map(PerfumeNotesList::new)
                .collect(Collectors.toList());
        this.heartNotes = perfume.getHeartNotes().stream()
                .map(PerfumeNotesList::new)
                .collect(Collectors.toList());
        this.baseNotes = perfume.getBaseNotes().stream()
                .map(PerfumeNotesList::new)
                .collect(Collectors.toList());
        if(perfume.getVotes() == 0){
            this.ratingPoints = 0;
            this.scentPoints = 0;
            this.longevityPoints = 0;
            this.sillagePoints = 0;
            this.bottlePoints = 0;
            this.valueOfMoneyPoints = 0;
        }else{
            this.ratingPoints = Math.round(perfume.getRatingPoints()/(double)perfume.getVotes()*10)/10.0;
            this.scentPoints = Math.round(perfume.getScentPoints()/(double)perfume.getVotes()*10)/10.0;
            this.longevityPoints = Math.round(perfume.getLongevityPoints()/(double)perfume.getVotes()*10)/10.0;
            this.sillagePoints = Math.round(perfume.getSillagePoints()/(double)perfume.getVotes()*10)/10.0;
            this.bottlePoints = Math.round(perfume.getBottlePoints()/(double)perfume.getVotes()*10)/10.0;
            this.valueOfMoneyPoints = Math.round(perfume.getValueOfMoneyPoints()/(double) perfume.getVotes()*10)/10.0;
        }

        this.votes = perfume.getVotes();

        this.type = perfume.getType();
        this.season = perfume.getSeason();
        this.occasion = perfume.getOccasion();
        this.audience = perfume.getAudience();

        this.description = perfume.getDescription();
        this.perfumer = perfume.getPerfumer();
        this.comments = perfume.getComments().stream()
                .map(PerfumeCommentResponse::new)
                .collect(Collectors.toList());
        this.images = perfume.getImages().stream()
                .map(PerfumeImgResponse::new)
                .collect(Collectors.toList());
        this.view = perfume.getView();
    }
}
