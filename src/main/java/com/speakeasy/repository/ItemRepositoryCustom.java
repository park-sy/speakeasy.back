package com.speakeasy.repository;

import com.speakeasy.domain.Item;
import com.speakeasy.request.ItemSearch;

import java.util.List;

public interface ItemRepositoryCustom {
    List<Item> getList(ItemSearch itemSearch);
}
