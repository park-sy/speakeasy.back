package com.speakeasy.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.speakeasy.domain.perfume.Note;
import com.speakeasy.domain.perfume.Perfume;
import com.speakeasy.domain.perfume.PerfumeComment;
import com.speakeasy.domain.perfume.PerfumeImages;
import com.speakeasy.domain.user.User;
import com.speakeasy.repository.*;
import com.speakeasy.repository.perfume.NoteRepository;
import com.speakeasy.repository.perfume.PerfumeCommentRepository;
import com.speakeasy.repository.perfume.PerfumeImgRepository;
import com.speakeasy.repository.perfume.PerfumeRepository;
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
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PerfumeControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PerfumeRepository perfumeRepository;

    @Autowired
    private PerfumeCommentRepository perfumeCommentRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private PerfumeImgRepository perfumeImgRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean(){
        perfumeRepository.deleteAll();
        perfumeCommentRepository.deleteAll();
        perfumeImgRepository.deleteAll();
        userRepository.deleteAll();
        noteRepository.deleteAll();
    }

    @Test
    @DisplayName("아이템 페이지 조회")
    void test1() throws Exception {
        //given
        List<Perfume> requestPerfumes = IntStream.range(0,20)
                .mapToObj(i -> Perfume.builder()
                        .name("상품" +i)
                        .brand("브랜드"+i)
                        .perfumer("베이스"+i)
                        .build()).collect(Collectors.toList());
        perfumeRepository.saveAll(requestPerfumes);

        //expected
        mockMvc.perform(get("/perfumes")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()",is(10)))
                .andExpect(jsonPath("$[0].name").value("상품19"))
                .andExpect(jsonPath("$[0].brand").value("브랜드19"))
                .andDo(print());

    }
    @Test
    @DisplayName("아이템 페이지 조회/페이지,갯수 추가")
    void test2() throws Exception {
        //given
        List<Perfume> requestPerfumes = IntStream.range(0,20)
                .mapToObj(i -> Perfume.builder()
                        .name("상품" +i)
                        .brand("브랜드"+i)
                        .perfumer("베이스"+i)
                        .build()).collect(Collectors.toList());
        perfumeRepository.saveAll(requestPerfumes);

        //expected
        mockMvc.perform(get("/perfumes?page=2&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()",is(10)))
                .andExpect(jsonPath("$[0].name").value("상품9"))
                .andExpect(jsonPath("$[0].brand").value("브랜드9"))
                .andDo(print());

    }
    @Test
    @DisplayName("아이템 필터링 조회")
    void test3() throws Exception {
        //given
        List<Perfume> requestPerfumes = IntStream.range(0,20)
                .mapToObj(i -> Perfume.builder()
                        .name("상품" +i)
                        .brand("브랜드"+i)
                        .perfumer("베이스"+i)
                        .build()).collect(Collectors.toList());
        perfumeRepository.saveAll(requestPerfumes);
        List<Note> requestNotes = IntStream.range(0,20)
                        .mapToObj(i ->Note.builder()
                                .name("노트"+i)
                                .img("이미지"+i)
                                .build()).collect(Collectors.toList());
        noteRepository.saveAll(requestNotes);
        //expected
        mockMvc.perform(get("/perfumes?page=1&size=10&topNotes=1,2")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("아이템 상세정보")
    void test4() throws Exception {
        //given
        Perfume perfume = Perfume.builder()
                .name("상품")
                .ratingPoints(50L)
                .scentPoints(55L)
                .longevityPoints(30L)
                .sillagePoints(40L)
                .bottlePoints(10L)
                .valueOfMoneyPoints(46L)
                .votes(10L).build();

        perfumeRepository.save(perfume);

        //expected
        mockMvc.perform(get("/perfumes/{perfumeId}",perfume.getId())
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(perfume.getId()))
                .andExpect(jsonPath("$.name").value("상품"))
                .andDo(print());

    }

    @Test
    @DisplayName("아이템 상세정보 이미지")
    void test5() throws Exception {
        //given
        Perfume perfume = Perfume.builder()
                .name("상품").build();

        perfumeRepository.save(perfume);

        PerfumeImages images1 = PerfumeImages.builder()
                .originFileName("123")
                .newFileName("123")
                .perfume(perfume).build();
        perfumeImgRepository.save(images1);

        PerfumeImages images2 = PerfumeImages.builder()
                .originFileName("456")
                .newFileName("444")
                .perfume(perfume).build();
        perfumeImgRepository.save(images2);

        //expected
        mockMvc.perform(get("/perfumes/{perfumeId}/img",perfume.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 가져오기")
    void test6() throws Exception{
        //given
        Perfume perfume = Perfume.builder()
                .name("상품").build();
        perfumeRepository.save(perfume);

        User user = User.builder()
                .uid("userid")
                .password("123")
                .name("이름").build();
        userRepository.save(user);

        PerfumeComment parent = PerfumeComment.builder()
                .comment("부모")
                .user(user)
                .perfume(perfume).build();
        perfumeCommentRepository.save(parent);

        PerfumeComment children = PerfumeComment.builder()
                .comment("자식")
                .user(user)
                .parent(parent)
                .perfume(perfume).build();
        perfumeCommentRepository.save(children);

        PerfumeComment parent2 = PerfumeComment.builder()
                .comment("부모2")
                .user(user)
                .perfume(perfume).build();
        perfumeCommentRepository.save(parent2);

        //expected
        mockMvc.perform(get("/perfumes/{perfumeId}/comments",perfume.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제하기")
    void test7() throws Exception{
        //given
        Perfume perfume = Perfume.builder()
                .name("상품").build();
        perfumeRepository.save(perfume);

        User user = User.builder()
                .uid("userid")
                .password("123")
                .name("이름").build();
        userRepository.save(user);

        PerfumeComment parent = PerfumeComment.builder()
                .comment("부모")
                .user(user)
                .perfume(perfume).build();
        perfumeCommentRepository.save(parent);

        PerfumeComment children = PerfumeComment.builder()
                .comment("자식")
                .user(user)
                .parent(parent)
                .perfume(perfume).build();
        perfumeCommentRepository.save(children);

        PerfumeComment parent2 = PerfumeComment.builder()
                .comment("부모2")
                .user(user)
                .perfume(perfume).build();
        perfumeCommentRepository.save(parent2);

        //expected
        mockMvc.perform(delete("/perfumes/{perfumeId}/comments/{commentId}",perfume.getId(),parent.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(get("/perfumes/{perfumeId}/comments",perfume.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("아이템 조회수")
    void test8() throws Exception {
        //given
        Perfume perfume = Perfume.builder()
                .name("상품")
                .ratingPoints(50L)
                .scentPoints(55L)
                .longevityPoints(30L)
                .sillagePoints(40L)
                .bottlePoints(10L)
                .valueOfMoneyPoints(46L)
                .votes(10L).build();

        perfumeRepository.save(perfume);
        mockMvc.perform(get("/perfumes/{perfumeId}",perfume.getId()));
        mockMvc.perform(get("/perfumes/{perfumeId}",perfume.getId()));

        //expected
        mockMvc.perform(get("/perfumes/{perfumeId}",perfume.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(perfume.getId()))
                .andExpect(jsonPath("$.name").value("상품"))
                .andDo(print());


    }
}