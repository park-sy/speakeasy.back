package com.speakeasy.repository.perfume;

import com.speakeasy.domain.perfume.PerfumeComment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PerfumeCommentRepository extends JpaRepository<PerfumeComment, Long>,PerfumeCommentRepositoryCustom {

}
