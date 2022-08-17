package com.speakeasy.repository.perfume;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.speakeasy.domain.perfume.PerfumeComment;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

import static com.speakeasy.domain.perfume.QPerfumeComment.perfumeComment;


@RequiredArgsConstructor //자동으로 생성자 주입
@ToString
public class PerfumeCommentRepositoryImpl implements PerfumeCommentRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<PerfumeComment> getComment(Long perfumeId){
        return jpaQueryFactory
                .selectFrom(perfumeComment)
                .leftJoin(perfumeComment.parent)
                .fetchJoin()
                .where(perfumeComment.perfume.id.eq(perfumeId))
                .orderBy(
                        perfumeComment.parent.id.asc().nullsFirst(),
                        perfumeComment.createdDate.asc()
                ).fetch();
    };

}
