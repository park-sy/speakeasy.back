package com.speakeasy.service;


import com.speakeasy.domain.Item;
import com.speakeasy.domain.ItemComment;
import com.speakeasy.exception.ItemNotFound;
import com.speakeasy.repository.ItemCommentRepository;
import com.speakeasy.repository.ItemRepository;
import com.speakeasy.request.ItemCommentCreate;
import com.speakeasy.request.ItemSearch;
import com.speakeasy.response.ItemDetailResponse;
import com.speakeasy.response.ItemImgResponse;
import com.speakeasy.response.ItemResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j //로그 작성
@Service  //서비스 레이어
@RequiredArgsConstructor  //lombok을 통해 생성자처리
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemCommentRepository itemCommentRepository;

//    public List<ItemResponse> getList(ItemSearch itemSearch){
//        return itemRepository.getList(itemSearch).stream()
//                .map(ItemResponse::new)
//                .collect(Collectors.toList());
//    }

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

    public File[] getImg(Long itemId) {

        ItemImgResponse itemImg =  ItemImgResponse.builder()
                                        .id(itemId).build();

        return itemImg.getImg(itemImg.getId());
    }

    public Long write(Long id, ItemCommentCreate create) {
        Item item = itemRepository.findById(id)
                .orElseThrow(ItemNotFound::new);
        create.setItem(item);

        ItemComment itemComment = create.toEntity();
        itemCommentRepository.save(itemComment);

        return create.getId();
    }

    public void commentEdit(Long id, ItemCommentCreate edit) {
        ItemComment itemComment = itemCommentRepository.findById(id)
                .orElseThrow(ItemNotFound::new);

        itemComment.edit(edit.getComment());
    }

    public void commentDelete(Long id) {
        ItemComment itemComment = itemCommentRepository.findById(id)
                .orElseThrow(ItemNotFound::new);
        itemCommentRepository.delete(itemComment);
    }


}
