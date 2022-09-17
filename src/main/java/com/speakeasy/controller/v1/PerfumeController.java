package com.speakeasy.controller.v1;


import com.speakeasy.request.perfume.PerfumeCommentCreate;
import com.speakeasy.request.perfume.PerfumeSearch;
import com.speakeasy.request.perfume.PerfumeVoteUpdate;
import com.speakeasy.response.perfume.*;
import com.speakeasy.service.PerfumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController // 결과값을 JSON으로 출력
@RequiredArgsConstructor ////lombok을 통해 생성자처리
public class PerfumeController {

    private final PerfumeService perfumeService;

    @GetMapping("/perfumes")
    public List<PerfumeResponse> getList(@ModelAttribute PerfumeSearch perfumeSearch){
        return perfumeService.getList(perfumeSearch);
    }

    @GetMapping("/perfumes/{perfumeId}")
    public PerfumeDetailResponse get(@PathVariable Long perfumeId){
        perfumeService.updateView(perfumeId);
        return perfumeService.get(perfumeId);
    }
    @GetMapping("/perfumes/{perfumeId}/img")
    public List<PerfumeImgResponse> getImg(@PathVariable Long perfumeId){
        return perfumeService.getImg(perfumeId);
    }
    @GetMapping("/perfumes/{perfumeId}/comments") //User
    public List<PerfumeCommentResponse> getComment(@PathVariable Long perfumeId){
        return perfumeService.getComment(perfumeId);
    }
    @PostMapping("/perfumes/{perfumeId}/comments") //User
    public ResponseEntity commentSave(@PathVariable Long id, @RequestBody PerfumeCommentCreate create){
        return ResponseEntity.ok(perfumeService.write(id,create));
    }

    @PatchMapping("/perfumes/{perfumeId}/comments/{id}")
    public void editComment(@PathVariable Long id, @RequestBody @Valid PerfumeCommentCreate edit){
        perfumeService.commentEdit(id,edit);
    }
    @DeleteMapping("/perfumes/{perfumeId}/comments/{id}")
    public ResponseEntity commentDelete(@PathVariable Long id){
        perfumeService.commentDelete(id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/perfumes/{perfumeId}/vote")
    public List<PerfumeVoteResponse> updateVote(@PathVariable Long perfumeId){
        return perfumeService.getVote(perfumeId);
    }

}
