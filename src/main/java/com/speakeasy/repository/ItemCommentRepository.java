package com.speakeasy.repository;

import com.speakeasy.domain.ItemComment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemCommentRepository extends JpaRepository<ItemComment, Long> {

}
