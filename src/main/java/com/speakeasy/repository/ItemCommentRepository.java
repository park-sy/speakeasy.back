package com.speakeasy.repository;

import com.speakeasy.domain.Item;
import com.speakeasy.domain.ItemComment;
import com.speakeasy.response.ItemCommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ItemCommentRepository extends JpaRepository<ItemComment, Long>,ItemCommentRepositoryCustom {

}
