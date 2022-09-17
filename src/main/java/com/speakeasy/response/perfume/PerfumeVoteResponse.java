package com.speakeasy.response.perfume;

import com.speakeasy.domain.perfume.PerfumeVote;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class PerfumeVoteResponse {
    private String note;
    private double percent;
    private Long vote;

    public PerfumeVoteResponse(String note, Long vote, Long total){
        this.note = note;
        this.vote = vote;
        this.percent = (double) vote/total * 100 / 1.0;
    }
}
