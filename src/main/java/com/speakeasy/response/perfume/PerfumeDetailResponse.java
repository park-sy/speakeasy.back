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

    private final String description;
    private final String perfumer;
    private List<PerfumeImgResponse> images;
    private List<PerfumeCommentResponse> comments;

    private final int view;



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
