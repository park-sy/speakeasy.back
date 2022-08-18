package com.speakeasy.repository.perfume;

import com.speakeasy.domain.perfume.PerfumeComment;

import java.util.List;

public interface PerfumeCommentRepositoryCustom {
    List<PerfumeComment> getComment(Long perfumeId);


}
