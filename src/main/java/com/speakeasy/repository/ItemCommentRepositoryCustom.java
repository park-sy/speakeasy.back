package com.speakeasy.repository;

import com.speakeasy.domain.Item;
import com.speakeasy.domain.ItemComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemCommentRepositoryCustom {
    List<ItemComment> getComment(Long itemId);


}
