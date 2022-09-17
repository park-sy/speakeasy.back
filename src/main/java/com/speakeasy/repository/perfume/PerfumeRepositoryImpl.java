package com.speakeasy.repository.perfume;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.speakeasy.domain.perfume.Perfume;

import com.speakeasy.domain.perfume.QPerfumeVote;
import com.speakeasy.request.perfume.PerfumeSearch;
import com.speakeasy.response.perfume.PerfumeResponse;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.speakeasy.domain.perfume.QNote.note;
import static com.speakeasy.domain.perfume.QPerfume.perfume;
import static com.speakeasy.domain.perfume.QPerfumeScore.perfumeScore;
import static com.speakeasy.domain.perfume.QPerfumeTypeNote.perfumeTypeNote;
import static com.speakeasy.domain.perfume.QPerfumeTypeSub.perfumeTypeSub;
import static com.speakeasy.domain.perfume.QPerfumeVote.perfumeVote;


@RequiredArgsConstructor //자동으로 생성자 주입
@ToString
public class PerfumeRepositoryImpl implements PerfumeRepositoryCustom{


    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<PerfumeResponse> getList(PerfumeSearch perfumeSearch){

        return jpaQueryFactory
                .select(Projections.constructor(PerfumeResponse.class,
                        perfume.id,perfume.name,perfume.brand, perfume.points))
                .from(perfume)
                .leftJoin(perfumeScore).on(perfume.id.eq(perfumeScore.perfume.id))
                .where(
                        eqSeason(perfumeSearch.getSeason()),
                        eqOccasion(perfumeSearch.getOccasion()),
                        eqAudience(perfumeSearch.getAudience()),
                        eqBrand(perfumeSearch.getBrand()),
                        goePoints(perfumeSearch.getGoePoints()),
                        loePoints(perfumeSearch.getLoePoints()),
                        eqType(perfumeSearch.getTopNotes()))
                .groupBy(perfume.id)
                .having(score(perfumeSearch.getGoePoints()))
                .limit(perfumeSearch.getSize())
                .offset(perfumeSearch.getOffset())
                .orderBy(sortOrder(perfumeSearch.getOrder()))
                .fetch();
    }

    //BooleanExpression을 통한 동적 쿼리문 작성

    private OrderSpecifier<?> sortOrder(Integer order){
        if(order == 1) return perfumeScore.point.avg().desc();
        else if (order == 2) return perfume.name.asc();
        return perfume.id.desc();
    }
    private BooleanExpression eqSeason(List<String> season){
        if(season == null) return null;
        return perfume.season.in(season);
    }
    private BooleanExpression eqBrand(List<String> brand){
        if(brand == null) return null;
        return perfume.brand.in(brand);
    }

    private BooleanExpression eqType(List<Long> type){
        if(type == null) return null;

        List<Tuple> ids = jpaQueryFactory.select(perfumeTypeNote.perfume.id,perfumeTypeSub.flag).from(perfumeTypeNote)
                .leftJoin(perfumeTypeSub).on(perfumeTypeNote.perfume.id.eq(perfumeTypeSub.perfume_id))
                .groupBy(perfumeTypeNote.perfume,perfumeTypeNote.note)
                .where(perfumeTypeNote.note.id.in(type))
                .having(perfumeTypeSub.flag.loe(perfumeTypeNote.note.count()))
                .fetch();
        List<Long> id = new ArrayList<>();
        for (Tuple t : ids){
            id.add(t.get(perfumeTypeNote.perfume.id));
        }
        return perfume.id.in(id);
    }

    private BooleanExpression eqOccasion(List<String> occasion){
        if(occasion == null) return null;
        return perfume.occasion.in(occasion);
    }

    private BooleanExpression eqAudience(List<String> audience){
        if(audience == null) return null;
        return perfume.audience.in(audience);
    }

    private BooleanExpression goePoints(Integer avgPoints){
        if(avgPoints == null){
            return null;
        }
        return perfume.points.divide(perfume.votes).goe(avgPoints);
    }

    private BooleanExpression loePoints(Integer avgPoints){
        if(avgPoints == null){
            return null;
        }
        return perfume.points.divide(perfume.votes).loe(avgPoints);
    }

    private BooleanExpression score(Integer avgPoints){
        if(avgPoints == null) return null;
        NumberExpression<Integer> scent = perfumeScore.score.id.
                when(1L).then(perfumeScore.point).
                otherwise(0);
        NumberExpression<Integer> longevity = perfumeScore.score.id.
                when(2L).then(perfumeScore.point).
                otherwise(0);
        NumberExpression<Integer> sillage = perfumeScore.score.id.
                when(3L).then(perfumeScore.point).
                otherwise(0);
        NumberExpression<Integer> valueformoney = perfumeScore.score.id.
                when(4L).then(perfumeScore.point).
                otherwise(0);

        return scent.avg().goe(avgPoints);
    }


    @Override
    @Transactional
    public void updateView(Long perfumeId){
        jpaQueryFactory.update(perfume)
                .set(perfume.view, perfume.view.add(1))
                .where(perfume.id.eq(perfumeId))
                .execute();
    }

    @Override
    @Transactional
    public void updatePoint(Long perfumeId){
        List<Double> points = jpaQueryFactory.select(perfumeScore.point.avg()).from(perfumeScore)
                .where(perfumeScore.perfume.id.eq(perfumeId))
                .groupBy(perfumeScore.score.id)
                .fetch();
        Double value =points.stream().mapToDouble(Double::doubleValue).average().orElse(0);

        jpaQueryFactory.update(perfume)
                .set(perfume.points, value)
                .where(perfume.id.eq(perfumeId))
                .execute();
    }


    private BooleanExpression eqOccasion1(Long vote,String occasion){
        if(occasion == null) return null;
        NumberExpression<Integer> tpo = perfumeVote.value.
                when(occasion).then(1).
                otherwise(0);
//        NumberExpression<Integer> business = perfumeVote.value.
//                when("business").then(1).
//                otherwise(0);
//        NumberExpression<Integer> sporty = perfumeVote.value.
//                when("sporty").then(1).
//                otherwise(0);
//        NumberExpression<Integer> vacation = perfumeVote.value.
//                when("vacation").then(1).
//                otherwise(0);
//        NumberExpression<Integer> party = perfumeVote.value.
//                when("party").then(1).
//                otherwise(0);
//        NumberExpression<Integer> date = perfumeVote.value.
//                when("date").then(1).
//                otherwise(0);
        List<Long> id = jpaQueryFactory.select(perfumeVote.perfume.id).from(perfumeVote)
                .groupBy(perfumeVote.perfume.id)
                .where(perfumeVote.vote.id.eq(vote))
                .having(tpo.count().goe(perfumeVote.count().divide(perfumeVote.note.countDistinct())))
                .fetch();

        return perfume.id.in(id);
    }
}
