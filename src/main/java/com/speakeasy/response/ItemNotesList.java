package com.speakeasy.response;

import com.speakeasy.domain.Note;
import lombok.Getter;

@Getter
public class ItemNotesList {

    private final String name;
    private final String img;

    public ItemNotesList(Note note){
        this.name = note.getName();
        this.img = note.getImg();
    }
}
