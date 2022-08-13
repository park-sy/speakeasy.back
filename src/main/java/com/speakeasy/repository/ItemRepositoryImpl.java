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
//                        eqtopNotes(itemSearch.getTopNotes()),
                        eqbrand(itemSearch.getBrand())
//                        search(itemSearch.getSearchKey())
                )
                .limit(itemSearch.getSize())
                .offset(itemSearch.getOffset())
                .orderBy(item.id.desc())
                .fetch();
    }

    //BooleanExpression을 통한 동적 쿼리문 작성
//    private BooleanExpression eqtopNotes(List<String> topNotes){
//        if(topNotes == null){
//            return null;
//        }
//        item.topNotes.in(topNotes).and(item.topNotes.like("11"));
//        return item.topNotes.in(topNotes);
//    }

    private BooleanExpression eqbrand(List<String> brand){
        if(brand == null){
            return null;
        }
        return item.brand.in(brand);
    }

//    private BooleanExpression search(List<String> searchKey){
//        if(searchKey == null){
//            return null;
//        }
//        return item.brand.like(searchKey.toString())
//                .and(item.topNotes.contains(searchKey.toString()));
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

