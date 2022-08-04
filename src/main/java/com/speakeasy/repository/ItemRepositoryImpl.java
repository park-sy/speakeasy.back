package com.speakeasy.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.speakeasy.domain.Item;
import com.speakeasy.domain.QItem;
import com.speakeasy.request.ItemSearch;
import com.speakeasy.response.ItemResponse;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.speakeasy.domain.QItem.item;

@RequiredArgsConstructor //자동으로 생성자 주입
@ToString
public class ItemRepositoryImpl implements ItemRepositoryCustom{


    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Item> getList(ItemSearch itemSearch){
        //JPAQueryFactory의 내무 메소드를 통해 페이지 규격 설정
        return jpaQueryFactory
                .selectFrom(item)
                .where(
                        eqNote(itemSearch.getNote()),
                        eqIncense(itemSearch.getIncense())
//                        search(itemSearch.getSearchKey())
                )
                .limit(itemSearch.getSize())
                .offset(itemSearch.getOffset())
                .orderBy(item.id.desc())
                .fetch();
    }

    //BooleanExpression을 통한 동적 쿼리문 작성
    private BooleanExpression eqNote(List<String> note){
        if(note == null){
            return null;
        }
        item.note.in(note).and(item.note.like("11"));
        return item.note.in(note);
    }

    private BooleanExpression eqIncense(List<String> incense){
        if(incense == null){
            return null;
        }
        return item.incense.in(incense);
    }

//    private BooleanExpression search(List<String> searchKey){
//        if(searchKey == null){
//            return null;
//        }
//        return item.incense.like(searchKey.toString())
//                .and(item.note.contains(searchKey.toString()));
//    }


//    private BooleanExpression goeMinPrice(Integer minPrice){
//        if(minPrice == null){
//            return null;
//        }
//        return item.minPrice.goe(minPrice);
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

