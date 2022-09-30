package com.speakeasy.repository.perfume;

import com.speakeasy.response.perfume.PerfumeVoteResponse;

import java.util.List;

public interface PerfumeVoteRepositoryCustom {
    List<PerfumeVoteResponse> getVote(Long perfumeId);
}
