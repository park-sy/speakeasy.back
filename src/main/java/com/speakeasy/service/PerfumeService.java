package com.speakeasy.service;


import com.speakeasy.domain.perfume.Perfume;
import com.speakeasy.domain.perfume.PerfumeComment;
import com.speakeasy.exception.PerfumeNotFound;
import com.speakeasy.repository.perfume.PerfumeCommentRepository;
import com.speakeasy.repository.perfume.PerfumeImgRepository;
import com.speakeasy.repository.perfume.PerfumeRepository;
import com.speakeasy.repository.UserRepository;
import com.speakeasy.request.perfume.PerfumeCommentCreate;
import com.speakeasy.request.perfume.PerfumeSearch;
import com.speakeasy.response.perfume.PerfumeCommentResponse;
import com.speakeasy.response.perfume.PerfumeDetailResponse;
import com.speakeasy.response.perfume.PerfumeImgResponse;
import com.speakeasy.response.perfume.PerfumeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j //로그 작성
@Service  //서비스 레이어
@RequiredArgsConstructor  //lombok을 통해 생성자처리
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;
    private final PerfumeCommentRepository perfumeCommentRepository;
    private final PerfumeImgRepository perfumeImgRepository;
    private final UserRepository userRepository;


    public List<PerfumeResponse> getList(PerfumeSearch perfumeSearch){
        return perfumeRepository.getList(perfumeSearch).stream()
                .map(PerfumeResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PerfumeDetailResponse get(Long perfumeId) {
        Perfume perfume = perfumeRepository.findById(perfumeId)
                .orElseThrow(PerfumeNotFound::new);
        PerfumeDetailResponse perfumeDetailResponse = new PerfumeDetailResponse(perfume);
        return perfumeDetailResponse;
    }

    public List<PerfumeImgResponse> getImg(Long perfumeId){
        Perfume perfume = perfumeRepository.findById(perfumeId)
                .orElseThrow(PerfumeNotFound::new);
        return perfumeImgRepository.findByPerfume(perfume).stream()
                .map(PerfumeImgResponse::new)
                .collect(Collectors.toList());
    }
    public List<PerfumeCommentResponse> getComment(Long perfumeId) {
        List<PerfumeCommentResponse> result = new ArrayList<>();
        Map<Long, PerfumeCommentResponse> map = new HashMap<>();
        perfumeCommentRepository.getComment(perfumeId).stream().forEach(c->{
            PerfumeCommentResponse dto = new PerfumeCommentResponse(c);
            map.put(dto.getId(),dto);
            if(c.getParent() != null) map.get(c.getParent().getId()).getChildren().add(dto);
            else result.add(dto);
        });
        return result;

    }

    public Long write(Long id, PerfumeCommentCreate create) {
        Perfume perfume = perfumeRepository.findById(id)
                .orElseThrow(PerfumeNotFound::new);
        if(create.getParentID() != null){
            PerfumeComment parent = perfumeCommentRepository.findById(create.getParentID())
                    .orElseThrow(PerfumeNotFound::new);
            create.setParent(parent);
        }

        //User user = userRepository.findBy(userId);


        create.setPerfume(perfume);
        //create.setUser(user);

        PerfumeComment perfumeComment = create.toEntity();
        perfumeCommentRepository.save(perfumeComment);

        return create.getId();
    }
    @Transactional
    public void commentEdit(Long id, PerfumeCommentCreate edit) {
        PerfumeComment perfumeComment = perfumeCommentRepository.findById(id)
                .orElseThrow(PerfumeNotFound::new);

        perfumeComment.edit(edit.getComment());
    }

//    public void commentDelete(Long id) {
//        PerfumeComment perfumeComment = perfumeCommentRepository.findById(id)
//                .orElseThrow(PerfumeNotFound::new);
//        perfumeCommentRepository.delete(perfumeComment);
//    }
    @Transactional
    public void commentDelete(Long id) {
        PerfumeComment perfumeComment = perfumeCommentRepository.findById(id)
                .orElseThrow(PerfumeNotFound::new);
        perfumeComment.edit("삭제된 댓글 입니다.");
    }


    public void updateView(Long perfumeId) {
        perfumeRepository.updateView(perfumeId);
    }

//    public void updateSeason(Long id, PerfumeSeasonUpdate perfumeSeasonUpdate) {
//        Perfume perfume = perfumeRepository.findById(id)
//                .orElseThrow(PerfumeNotFound::new);
//        HashMap<String, Long> season = perfume.getSeason();
//
//        Long votes = season.get("spring");
//        season.put("spring",votes+1L);
//
//
//    }
}
