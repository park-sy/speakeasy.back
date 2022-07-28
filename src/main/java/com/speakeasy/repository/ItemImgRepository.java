package com.speakeasy.repository;

import com.speakeasy.domain.Item;
import com.speakeasy.domain.ItemImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImages, Long> {
    List<ItemImages> findByItem(Item item);
}
