package com.speakeasy.response.perfume;

import com.speakeasy.domain.perfume.Note;
import lombok.Getter;

@Getter
public class PerfumeNotesList {

    private final String name;
    private final String img;

    public PerfumeNotesList(Note note){
        this.name = note.getName();
        this.img = note.getImg();
    }
}
