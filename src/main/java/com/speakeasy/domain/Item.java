package com.speakeasy.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity  //데이터베이스에 사용될 entity를 정의
@Getter  //getter 자동 선언
@NoArgsConstructor(access = AccessLevel.PUBLIC)  //인자없는 생성자를 자동 생성
public class Item {

    @Id  //PK임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //pk 설정을 DB에게 위임,
    private Long id;

    private String name;
    private String note;
    private String incense;
    private String season;
    private String base;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc")
    private List<ItemComment> comments ;
    @Builder //Builder 패턴 사용
    public Item(String name, String note, String incense, String season, String base) {
        this.name = name;
        this.note = note;
        this.incense = incense;
        this.season = season;
        this.base = base;
    }


}