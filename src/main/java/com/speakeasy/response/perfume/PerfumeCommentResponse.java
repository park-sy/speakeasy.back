package com.speakeasy.response.perfume;

import com.speakeasy.domain.perfume.PerfumeComment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PerfumeCommentResponse {
    private Long id;
    private String comment;
    private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));

    private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private String uid;
    private Long perfumeId;

    private List<PerfumeCommentResponse> children = new ArrayList<>();


   public PerfumeCommentResponse(PerfumeComment perfumeComment){
       this.id = perfumeComment.getId();
       this.comment = perfumeComment.getComment();
       this.createdDate = perfumeComment.getCreatedDate();
       this.modifiedDate =perfumeComment.getModifiedDate();
       this.uid = perfumeComment.getUser().getName();
       this.perfumeId = perfumeComment.getPerfume().getId();
   }

}
