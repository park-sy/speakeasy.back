package com.speakeasy.repository.perfume;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.speakeasy.domain.perfume.QNote;
import com.speakeasy.response.perfume.PerfumeVoteResponse;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

import static com.speakeasy.domain.perfume.QNote.note;
import static com.speakeasy.domain.perfume.QPerfume.perfume;
import static com.speakeasy.domain.perfume.QPerfumeTypeNote.perfumeTypeNote;
import static com.speakeasy.domain.perfume.QPerfumeVote.perfumeVote;

@RequiredArgsConstructor //자동으로 생성자 주입
@ToString
public class PerfumeVoteRepositoryImpl implements PerfumeVoteRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PerfumeVoteResponse> getVote(Long perfumeId){
        JPAQuery<Long> total = jpaQueryFactory.select(perfumeVote.id.count())
                .from(perfumeVote)
                .where(perfumeVote.perfume.id.eq(perfumeId));

        return jpaQueryFactory.select(Projections.constructor(PerfumeVoteResponse.class,
                        perfumeVote.note.name,perfumeVote.note.count(),total))
                .from(perfumeVote)
                .where(perfumeVote.perfume.id.eq(perfumeId))
                .groupBy(perfumeVote.note).fetch();
    }
}
