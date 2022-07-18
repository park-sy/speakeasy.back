package com.speakeasy.service;


import com.speakeasy.domain.Item;
import com.speakeasy.repository.ItemRepository;
import com.speakeasy.request.ItemSearch;
import com.speakeasy.response.ItemResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j //로그 작성
@Service  //서비스 레이어
@RequiredArgsConstructor  //lombok을 통해 생성자처리
public class ItemService {

    private final ItemRepository itemRepository;

    public List<ItemResponse> getList(ItemSearch itemSearch){
        return itemRepository.getList(itemSearch).stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }

}
