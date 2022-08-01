package com.speakeasy.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.speakeasy.domain.Item;
import com.speakeasy.domain.ItemComment;
import com.speakeasy.domain.ItemImages;
import com.speakeasy.domain.User;
import com.speakeasy.repository.ItemCommentRepository;
import com.speakeasy.repository.ItemImgRepository;
import com.speakeasy.repository.ItemRepository;
import com.speakeasy.repository.UserRepository;
import com.speakeasy.request.ItemCommentCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemCommentRepository itemCommentRepository;

    @Autowired
    private ItemImgRepository itemImgRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean(){
        itemRepository.deleteAll();
        itemCommentRepository.deleteAll();
        itemImgRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("아이템 페이지 조회")
    void test1() throws Exception {
        //given
        List<Item> requestItems = IntStream.range(0,20)
                .mapToObj(i -> Item.builder()
                        .name("상품" +i)
                        .note("노트"+i)
                        .incense("향"+i)
                        .season("계절"+i)
                        .base("베이스"+i)
                        .build()).collect(Collectors.toList());
        itemRepository.saveAll(requestItems);

        //expected
        mockMvc.perform(get("/items")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()",is(10)))
                .andExpect(jsonPath("$[0].name").value("상품19"))
                .andExpect(jsonPath("$[0].note").value("노트19"))
                .andDo(print());

    }
    @Test
    @DisplayName("아이템 페이지 조회/페이지,갯수 추가")
    void test2() throws Exception {
        //given
        List<Item> requestItems = IntStream.range(0,20)
                .mapToObj(i -> Item.builder()
                        .name("상품" +i)
                        .note("노트"+i)
                        .incense("향"+i)
                        .season("계절"+i)
                        .base("베이스"+i)
                        .build()).collect(Collectors.toList());
        itemRepository.saveAll(requestItems);

        //expected
        mockMvc.perform(get("/items?page=2&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()",is(10)))
                .andExpect(jsonPath("$[0].name").value("상품9"))
                .andExpect(jsonPath("$[0].note").value("노트9"))
                .andDo(print());

    }
    @Test
    @DisplayName("아이템 필터링 조회")
    void test3() throws Exception {
        //given
        List<Item> requestItems = IntStream.range(0,20)
                .mapToObj(i -> Item.builder()
                        .name("상품" +i)
                        .note("노트"+i)
                        .incense("향"+i)
                        .season("계절"+i)
                        .base("베이스"+i)
                        .build()).collect(Collectors.toList());
        itemRepository.saveAll(requestItems);

        //expected
        mockMvc.perform(get("/items?page=1&size=10&incense=향1,향2")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].note").value("노트2"))
                .andDo(print());

    }

    @Test
    @DisplayName("아이템 상세정보")
    void test4() throws Exception {
        //given
        Item item = Item.builder()
                .name("상품")
                .season("계절").build();

        itemRepository.save(item);

        //expected
        mockMvc.perform(get("/items/{itemId}",item.getId())
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value("상품"))
                .andDo(print());

    }

    @Test
    @DisplayName("아이템 상세정보 이미지")
    void test5() throws Exception {
        //given
        Item item = Item.builder()
                .name("상품")
                .season("계절").build();

        itemRepository.save(item);

        ItemImages images1 = ItemImages.builder()
                .originFileName("123")
                .newFileName("123")
                .item(item).build();
        itemImgRepository.save(images1);

        ItemImages images2 = ItemImages.builder()
                .originFileName("456")
                .newFileName("444")
                .item(item).build();
        itemImgRepository.save(images2);

        //expected
        mockMvc.perform(get("/items/{itemId}/img",item.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 가져오기")
    void test6() throws Exception{
        //given
        Item item = Item.builder()
                .name("상품")
                .season("계절").build();
        itemRepository.save(item);

        User user = User.builder()
                .uid("userid")
                .password("123")
                .name("이름").build();
        userRepository.save(user);

        ItemComment parent = ItemComment.builder()
                .comment("부모")
                .user(user)
                .item(item).build();
        itemCommentRepository.save(parent);

        ItemComment children = ItemComment.builder()
                .comment("자식")
                .user(user)
                .parent(parent)
                .item(item).build();
        itemCommentRepository.save(children);

        ItemComment parent2 = ItemComment.builder()
                .comment("부모2")
                .user(user)
                .item(item).build();
        itemCommentRepository.save(parent2);

        //expected
        mockMvc.perform(get("/items/{itemId}/comments",item.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제하기")
    void test7() throws Exception{
        //given
        Item item = Item.builder()
                .name("상품")
                .season("계절").build();
        itemRepository.save(item);

        User user = User.builder()
                .uid("userid")
                .password("123")
                .name("이름").build();
        userRepository.save(user);

        ItemComment parent = ItemComment.builder()
                .comment("부모")
                .user(user)
                .item(item).build();
        itemCommentRepository.save(parent);

        ItemComment children = ItemComment.builder()
                .comment("자식")
                .user(user)
                .parent(parent)
                .item(item).build();
        itemCommentRepository.save(children);

        ItemComment parent2 = ItemComment.builder()
                .comment("부모2")
                .user(user)
                .item(item).build();
        itemCommentRepository.save(parent2);

        //expected
        mockMvc.perform(delete("/items/{itemId}/comments/{commentId}",item.getId(),parent.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(get("/items/{itemId}/comments",item.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}