package com.speakeasy.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.speakeasy.domain.Item;
import com.speakeasy.domain.QItem;
import com.speakeasy.request.ItemSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.speakeasy.domain.QItem.item;

@RequiredArgsConstructor //자동으로 생성자 주입
public class ItemRepositoryImpl implements ItemRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Item> getList(ItemSearch itemSearch){
        //JPAQueryFactory의 내무 메소드를 통해 페이지 규격 설정
        return jpaQueryFactory.selectFrom(item)
                .where(
                        eqNote(itemSearch.getNote()),
                        eqIncense(itemSearch.getIncense())
                )
                .limit(itemSearch.getSize())
                .offset(itemSearch.getOffset())
                .orderBy(item.id.desc())
                .fetch();
    }
    //BooleanExpression을 통한 동적 쿼리문 작성
    private BooleanExpression eqNote(String note){
        if(StringUtils.isNullOrEmpty(note)){
            return null;
        }
        return item.note.eq(note);
    }

    private BooleanExpression eqIncense(String incense){
        if(StringUtils.isNullOrEmpty(incense)){
            return null;
        }
        return item.incense.eq(incense);
    }
}
