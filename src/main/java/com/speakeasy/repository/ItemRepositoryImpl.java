package com.speakeasy.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.speakeasy.domain.Item;
import com.speakeasy.domain.QItem;
import com.speakeasy.request.ItemSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor //자동으로 생성자 주입
public class ItemRepositoryImpl implements ItemRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Item> getList(ItemSearch itemSearch){
        //JPAQueryFactory의 내무 메소드를 통해 페이지 규격 설정
        return jpaQueryFactory.selectFrom(QItem.item)
                .limit(itemSearch.getSize())
                .offset(itemSearch.getOffset())
                .orderBy(QItem.item.id.desc())
                .fetch();
    }
}
