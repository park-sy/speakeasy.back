package com.speakeasy.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.speakeasy.domain.Item;
import com.speakeasy.request.ItemSearch;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.speakeasy.domain.QItem.item;
import static com.speakeasy.domain.QNote.note;

@RequiredArgsConstructor //자동으로 생성자 주입
@ToString
public class ItemRepositoryImpl implements ItemRepositoryCustom{


    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Item> getList(ItemSearch itemSearch){
        //JPAQueryFactory의 내무 메소드를 통해 페이지 규격 설정
        return jpaQueryFactory
                .select(item).from(item)
                .where(
                        eqtopNotes(itemSearch.getTopNotes()),
                        eqbrand(itemSearch.getBrand()),
                        goePoints(itemSearch.getGoePoints()),
                        loePoints(itemSearch.getLoePoints())
                )
                .limit(itemSearch.getSize())
                .offset(itemSearch.getOffset())
                .orderBy(item.id.desc())
                .fetch();
    }
//    public List<Tuple> getList2(ItemSearch itemSearch){
//        return jpaQueryFactory
//                .select(item.id, item.name, item.brand,
//
//                .from(item)
//                .where()
//                .limit(itemSearch.getSize())
//                .offset(itemSearch.getOffset())
//                .orderBy(item.id.desc())
//                .fetch();
//    }
    //BooleanExpression을 통한 동적 쿼리문 작성
    private BooleanExpression eqtopNotes(List<Long> topNotes){
        if(topNotes == null){
            return null;
        }

        List<Long> itemId = jpaQueryFactory
                .select(item.id).from(item,note).join(item.topNotes,note)
                .where(note.id.in(topNotes)).fetch();
        return item.id.in(itemId);
    }
//    private BooleanExpression eqSeason(String season){
//        if(season == null) return null;
//        List<Map<String, Long>> hashMap = jpaQueryFactory
//                .select(item.season).from(item)
//                .where(item.season.)
//                .fetch();
//        return null;
//    }
    private BooleanExpression eqbrand(List<String> brand){
        if(brand == null){
            return null;
        }

        return item.brand.in(brand);
    }

    private BooleanExpression goePoints(float avgPoints){
        if(avgPoints == 0){
            return null;
        }
        return item.scentPoints.divide(item.votes).goe(avgPoints);
    }

    private BooleanExpression loePoints(float avgPoints){
        if(avgPoints == 0){
            return null;
        }
        return item.scentPoints.divide(item.votes).loe(avgPoints);
    }
//    private BooleanExpression search(List<String> searchKey){
//        if(searchKey == null){
//            return null;
//        }
//        return item.brand.like(searchKey.toString())
//                .and(item.topNotes.contains(searchKey.toString()));
//    }



    //    private BooleanExpression loeMinPrice(Integer minPrice){
//        if(minPrice == null){
//            return null;
//        }
//        return item.minPrice.loe(minPrice);
//    }
    @Override
    @Transactional
    public void updateView(Long itemId){
        jpaQueryFactory.update(item)
                .set(item.view, item.view.add(1))
                .where(item.id.eq(itemId))
                .execute();

    }

}

