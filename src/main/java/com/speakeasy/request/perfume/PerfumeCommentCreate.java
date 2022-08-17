package com.speakeasy.request.perfume;

import com.speakeasy.domain.perfume.Perfume;
import com.speakeasy.domain.perfume.PerfumeComment;
import com.speakeasy.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
public class PerfumeCommentCreate {

    private Long id;
    private String comment;
    @Builder.Default
    private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));

    @Builder.Default
    private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private Long parentID;
    private User user;

    private PerfumeComment parent = null;
    private Perfume perfume;


    public PerfumeComment toEntity(){
        PerfumeComment perfumeComment = PerfumeComment.builder()
                .id(id)
                .comment(comment)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .user(user)
                .parent(parent)
                .perfume(perfume)
                .build();
        return perfumeComment;
    }
}
