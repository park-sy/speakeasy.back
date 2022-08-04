package com.speakeasy.service;


import com.speakeasy.domain.Item;
import com.speakeasy.domain.ItemComment;
import com.speakeasy.domain.ItemImages;
import com.speakeasy.domain.User;
import com.speakeasy.exception.ItemNotFound;
import com.speakeasy.repository.ItemCommentRepository;
import com.speakeasy.repository.ItemImgRepository;
import com.speakeasy.repository.ItemRepository;
import com.speakeasy.repository.UserRepository;
import com.speakeasy.request.ItemCommentCreate;
import com.speakeasy.request.ItemSearch;
import com.speakeasy.response.ItemCommentResponse;
import com.speakeasy.response.ItemDetailResponse;
import com.speakeasy.response.ItemImgResponse;
import com.speakeasy.response.ItemResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j //로그 작성
@Service  //서비스 레이어
@RequiredArgsConstructor  //lombok을 통해 생성자처리
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemCommentRepository itemCommentRepository;
    private final ItemImgRepository itemImgRepository;
    private final UserRepository userRepository;


    public List<ItemResponse> getList(ItemSearch itemSearch){
        return itemRepository.getList(itemSearch).stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }

    public ItemDetailResponse get(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemNotFound::new);
        ItemDetailResponse itemDetailResponse = new ItemDetailResponse(item);
        return itemDetailResponse;
    }

    public List<ItemImgResponse> getImg(Long itemId){
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemNotFound::new);
        return itemImgRepository.findByItem(item).stream()
                .map(ItemImgResponse::new)
                .collect(Collectors.toList());
    }
    public List<ItemCommentResponse> getComment(Long itemId) {
        List<ItemCommentResponse> result = new ArrayList<>();
        Map<Long, ItemCommentResponse> map = new HashMap<>();
        itemCommentRepository.getComment(itemId).stream().forEach(c->{
            ItemCommentResponse dto = new ItemCommentResponse(c);
            map.put(dto.getId(),dto);
            if(c.getParent() != null) map.get(c.getParent().getId()).getChildren().add(dto);
            else result.add(dto);
        });
        return result;

    }

    public Long write(Long id, ItemCommentCreate create) {
        Item item = itemRepository.findById(id)
                .orElseThrow(ItemNotFound::new);
        if(create.getParentID() != null){
            ItemComment parent = itemCommentRepository.findById(create.getParentID())
                    .orElseThrow(ItemNotFound::new);
            create.setParent(parent);
        }

        //User user = userRepository.findBy(userId);


        create.setItem(item);
        //create.setUser(user);

        ItemComment itemComment = create.toEntity();
        itemCommentRepository.save(itemComment);

        return create.getId();
    }
    @Transactional
    public void commentEdit(Long id, ItemCommentCreate edit) {
        ItemComment itemComment = itemCommentRepository.findById(id)
                .orElseThrow(ItemNotFound::new);

        itemComment.edit(edit.getComment());
    }

//    public void commentDelete(Long id) {
//        ItemComment itemComment = itemCommentRepository.findById(id)
//                .orElseThrow(ItemNotFound::new);
//        itemCommentRepository.delete(itemComment);
//    }
    @Transactional
    public void commentDelete(Long id) {
        ItemComment itemComment = itemCommentRepository.findById(id)
                .orElseThrow(ItemNotFound::new);
        itemComment.edit("삭제된 댓글 입니다.");
    }


    public void updateView(Long itemId) {
        itemRepository.updateView(itemId);
    }
}
