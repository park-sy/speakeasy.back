package com.speakeasy.repository;

import com.speakeasy.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

//JPARepository를 통해 DB에 접근, 엔티티와 PK를 값으로 가짐
public interface ItemRepository extends JpaRepository<Item, Long>,ItemRepositoryCustom {


}
