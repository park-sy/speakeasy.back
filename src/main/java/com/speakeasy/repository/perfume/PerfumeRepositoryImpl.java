package com.speakeasy.repository.perfume;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.speakeasy.domain.perfume.Perfume;
import com.speakeasy.request.perfume.PerfumeSearch;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.speakeasy.domain.perfume.QPerfume.perfume;
import static com.speakeasy.domain.perfume.QNote.note;

@RequiredArgsConstructor //자동으로 생성자 주입
@ToString
public class PerfumeRepositoryImpl implements PerfumeRepositoryCustom{


    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Perfume> getList(PerfumeSearch perfumeSearch){
        //JPAQueryFactory의 내무 메소드를 통해 페이지 규격 설정
        return jpaQueryFactory
                .select(perfume).from(perfume)
                .where(
                        eqtopNotes(perfumeSearch.getTopNotes()),
                        eqbrand(perfumeSearch.getBrand()),
                        goePoints(perfumeSearch.getGoePoints()),
                        loePoints(perfumeSearch.getLoePoints())
                )
                .limit(perfumeSearch.getSize())
                .offset(perfumeSearch.getOffset())
                .orderBy(perfume.id.desc())
                .fetch();
    }
//    public List<Tuple> getList2(PerfumeSearch perfumeSearch){
//        return jpaQueryFactory
//                .select(perfume.id, perfume.name, perfume.brand,
//
//                .from(perfume)
//                .where()
//                .limit(perfumeSearch.getSize())
//                .offset(perfumeSearch.getOffset())
//                .orderBy(perfume.id.desc())
//                .fetch();
//    }
    //BooleanExpression을 통한 동적 쿼리문 작성
    private BooleanExpression eqtopNotes(List<Long> topNotes){
        if(topNotes == null){
            return null;
        }

        List<Long> perfumeId = jpaQueryFactory
                .select(perfume.id).from(perfume,note).join(perfume.topNotes,note)
                .where(note.id.in(topNotes)).fetch();
        return perfume.id.in(perfumeId);
    }
//    private BooleanExpression eqSeason(String season){
//        if(season == null) return null;
//        List<Map<String, Long>> hashMap = jpaQueryFactory
//                .select(perfume.season).from(perfume)
//                .where(perfume.season.)
//                .fetch();
//        return null;
//    }
    private BooleanExpression eqbrand(List<String> brand){
        if(brand == null){
            return null;
        }

        return perfume.brand.in(brand);
    }

    private BooleanExpression goePoints(Integer avgPoints){
        if(avgPoints == null){
            return null;
        }
        return perfume.scentPoints.divide(perfume.votes).goe(avgPoints);
    }

    private BooleanExpression loePoints(Integer avgPoints){
        if(avgPoints == null){
            return null;
        }
        return perfume.scentPoints.divide(perfume.votes).loe(avgPoints);
    }
//    private BooleanExpression search(List<String> searchKey){
//        if(searchKey == null){
//            return null;
//        }
//        return perfume.brand.like(searchKey.toString())
//                .and(perfume.topNotes.contains(searchKey.toString()));
//    }



    //    private BooleanExpression loeMinPrice(Integer minPrice){
//        if(minPrice == null){
//            return null;
//        }
//        return perfume.minPrice.loe(minPrice);
//    }
    @Override
    @Transactional
    public void updateView(Long perfumeId){
        jpaQueryFactory.update(perfume)
                .set(perfume.view, perfume.view.add(1))
                .where(perfume.id.eq(perfumeId))
                .execute();

    }

}

