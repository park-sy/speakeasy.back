package com.speakeasy.repository;

import com.querydsl.core.Tuple;
import com.speakeasy.domain.Item;
import com.speakeasy.request.ItemSearch;
import com.speakeasy.response.ItemResponse;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface ItemRepositoryCustom {

    List<Item> getList(ItemSearch itemSearch);

    void updateView(Long itemId);


}
