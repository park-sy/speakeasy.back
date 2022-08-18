package com.speakeasy.service;

import com.speakeasy.domain.perfume.Perfume;
import com.speakeasy.domain.perfume.PerfumeComment;
import com.speakeasy.domain.perfume.PerfumeImages;
import com.speakeasy.repository.perfume.PerfumeCommentRepository;
import com.speakeasy.repository.perfume.PerfumeImgRepository;
import com.speakeasy.repository.perfume.PerfumeRepository;
import com.speakeasy.request.perfume.PerfumeCommentCreate;
import com.speakeasy.request.perfume.PerfumeSearch;
import com.speakeasy.response.perfume.PerfumeResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PerfumeServiceTest {

    @Autowired
    private PerfumeService perfumeService;

    @Autowired
    private PerfumeRepository perfumeRepository;

    @Autowired
    private PerfumeCommentRepository perfumeCommentRepository;

    @Autowired
    private PerfumeImgRepository perfumeImgRepository;

    @BeforeEach
    void clean(){
        perfumeRepository.deleteAll();
    }

    @Test
    @DisplayName("아이템 등록")
    void test1(){
        Perfume requestPerfume = Perfume.builder()
                .name("상품").build();

        perfumeRepository.save(requestPerfume);

        Assertions.assertEquals(1L,perfumeRepository.count());
        Perfume perfume = perfumeRepository.findAll().get(0);
        assertEquals("상품", perfume.getName());
    }

    @Test
    @DisplayName("페이지 불러오기")
    void test2(){
        //given
        List<Perfume> requestPerfumes = IntStream.range(0,20)
                .mapToObj(i -> Perfume.builder()
                        .name("상품 " +i)
                        .build()).collect(Collectors.toList());
        perfumeRepository.saveAll(requestPerfumes);
        PerfumeSearch perfumeSearch = PerfumeSearch.builder()
                .page(1)
                .build();

        //when
        List<PerfumeResponse> perfumes = perfumeService.getList(perfumeSearch);

        //then
        assertEquals(10L, perfumes.size());
        assertEquals("상품 19", perfumes.get(0).getName());
        assertEquals("상품 15", perfumes.get(4).getName());


    }
    @Test
    @DisplayName("아이템 이미지 불러오기")
    void test3(){
        //given
        Perfume requestPerfume = Perfume.builder()
                .name("상품").build();

        perfumeRepository.save(requestPerfume);

        PerfumeCommentCreate create = PerfumeCommentCreate.builder()
                .comment("댓글입니다.")
                .perfume(requestPerfume).build();
        perfumeService.write(requestPerfume.getId(),create);

        PerfumeImages images1 = PerfumeImages.builder()
                .originFileName("123")
                .newFileName("123")
                .perfume(requestPerfume).build();
        perfumeImgRepository.save(images1);

        PerfumeImages images2 = PerfumeImages.builder()
                .originFileName("456")
                .newFileName("444")
                .perfume(requestPerfume).build();
        perfumeImgRepository.save(images2);

        Perfume perfume = perfumeRepository.findAll().get(0);
        assertEquals("상품", perfume.getName());
        Set<PerfumeImages> perfumeImages = perfume.getImages();
        System.out.println(perfume.getImages());
    }

    @Test
    @DisplayName("댓글 작성하기")
    void test4(){
        //given
        Perfume requestPerfume = Perfume.builder()
                .name("상품").build();

        perfumeRepository.save(requestPerfume);

        PerfumeCommentCreate create = PerfumeCommentCreate.builder()
                .comment("댓글입니다.")
                .perfume(requestPerfume).build();
        perfumeService.write(requestPerfume.getId(),create);

        assertEquals(1L, perfumeRepository.count());
        Perfume perfume = perfumeRepository.findAll().get(0);
        assertEquals("상품", perfume.getName());

        assertEquals(1L, perfumeCommentRepository.count());
        PerfumeComment perfumeComment = perfumeCommentRepository.findAll().get(0);
        assertEquals("댓글입니다.", perfumeComment.getComment());


    }
}