package com.speakeasy.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;

@Builder
@Entity  //데이터베이스에 사용될 entity를 정의
@Getter  //getter 자동 선언
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)  //인자없는 생성자를 자동 생성
public class ItemComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String comment;
    @Column(name = "created_date")
    @CreatedDate
    private String createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private String modifiedDate;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    public ItemComment(Long id, String comment, Item item) {
        this.id = id;
        this.comment = comment;
        this.item = item;
    }

    public void edit(String comment) {
        this.comment = comment;
    }
}
