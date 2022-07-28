package com.speakeasy.request;

import com.speakeasy.domain.Item;
import com.speakeasy.domain.ItemComment;
import com.speakeasy.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
public class ItemCommentCreate {

    private Long id;
    private String comment;
    @Builder.Default
    private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));

    @Builder.Default
    private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private User user;
    private Item item;

    public ItemComment toEntity(){
        ItemComment itemComment = ItemComment.builder()
                .id(id)
                .comment(comment)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .user(user)
                .item(item)
                .build();
        return itemComment;
    }
}
