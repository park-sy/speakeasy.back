package com.speakeasy.response;

import com.speakeasy.domain.Item;
import com.speakeasy.domain.ItemComment;
import com.speakeasy.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ItemCommentResponse {
    private Long id;
    private String comment;
    private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));

    private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private User user;
    private Long itemId;

   public ItemCommentResponse(ItemComment itemComment){
       this.id = itemComment.getId();
       this.comment = itemComment.getComment();
       this.createdDate = itemComment.getCreatedDate();
       this.modifiedDate =itemComment.getModifiedDate();
       this.user = itemComment.getUser();
       this.itemId = itemComment.getItem().getId();
   }

}