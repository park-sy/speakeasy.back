package com.speakeasy.domain;


import lombok.*;

import javax.persistence.*;

@Builder
@Entity  //데이터베이스에 사용될 entity를 정의
@Getter  //getter 자동 선언
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)  //인자없는 생성자를 자동 생성
public class ItemImages {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String originFileName;
    private String newFileName;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}
