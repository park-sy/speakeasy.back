package com.speakeasy.request.perfume;

import com.speakeasy.domain.User;
import com.speakeasy.domain.perfume.Perfume;
import com.speakeasy.domain.perfume.PerfumeVote;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfumeVoteUpdate {
    private Long id;
    private Perfume perfume;
    private User user;
    private String name;
    private String value;

    public PerfumeVote toEntity(){
        PerfumeVote perfumeVote = PerfumeVote.builder()
                .id(id)
                .perfume(perfume)
                .user(user)
                .name(name)
                .value(value)
                .build();
        return perfumeVote;
    }
}
