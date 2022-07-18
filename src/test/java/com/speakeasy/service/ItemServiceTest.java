package com.speakeasy.service;

import com.speakeasy.domain.Item;
import com.speakeasy.repository.ItemRepository;
import com.speakeasy.request.ItemSearch;
import com.speakeasy.response.ItemResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void clean(){
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("아이템 등록")
    void test1(){
        Item requestItem = Item.builder()
                .name("상품")
                .note("노트")
                .incense("향")
                .season("계절").build();

        itemRepository.save(requestItem);

        Assertions.assertEquals(1L,itemRepository.count());
        Item item = itemRepository.findAll().get(0);
        assertEquals("제목입니다.", item.getName());
        assertEquals("내용입니다.", item.getIncense());
    }

    @Test
    @DisplayName("페이지 불러오기")
    void test2(){
        //given
        List<Item> requestItems = IntStream.range(0,20)
                .mapToObj(i -> Item.builder()
                        .name("상품 " +i)
                        .note("노트 "+i)
                        .incense("향"+i)
                        .season("계절"+i)
                        .build()).collect(Collectors.toList());
        itemRepository.saveAll(requestItems);
        ItemSearch itemSearch = ItemSearch.builder()
                .page(1)
                .build();

        //when
        List<ItemResponse> items = itemService.getList(itemSearch);

        //then
        assertEquals(10L, items.size());
        assertEquals("상품 19", items.get(0).getName());
        assertEquals("상품 15", items.get(4).getName());


    }

}