package com.speakeasy.domain;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Builder
@Entity  //데이터베이스에 사용될 entity를 정의
@Getter  //getter 자동 선언
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)  //인자없는 생성자를 자동 생성
@TypeDef(name = "json", typeClass = JsonType.class)
public class Item {

    @Id  //PK임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //pk 설정을 DB에게 위임,
    @Column(name = "item_id")
    private Long id;
    private String name;
    private String brand;

    @Column(length = 3000)
    private String url;

    @Column(columnDefinition = "json")
    @Type(type = "json")
    private Set<Long> topNotes;
    @Column(columnDefinition = "json")
    @Type(type = "json")
    private Set<Long> heartNotes;

    @Column(columnDefinition = "json")
    @Type(type = "json")
    private Set<Long> baseNotes;
    @ManyToMany
    @JoinTable(name = "item_notes"
            ,joinColumns = @JoinColumn(name = "item_id")
            ,inverseJoinColumns = @JoinColumn(name = "note_id"))
    private Set<Note> tmpNotes;

    @ManyToMany
    @JoinTable(name = "item_notes"
            ,joinColumns = @JoinColumn(name = "item_id")
            ,inverseJoinColumns = @JoinColumn(name = "note_id"))
    private Set<Note> tmp2Notes;

    private Long ratingPoints;
    private Long ratingVotes;
    private Long scentPoints;
    private Long scentVotes;
    private Long longevityPoints;
    private Long longevityVotes;
    private Long sillagePoints;
    private Long sillageVotes;
    private Long bottlePoints;
    private Long bottleVotes;
    private Long valueOfMoneyPoints;
    private Long valueOfMoneyVotes;

    private String type;
    private String season;
    private String occasion;
    private String audience;
    private String description;

    private String perfumer;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc")
    private Set<ItemComment> comments;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc")
    private Set<ItemImages> images;

//    @Builder //Builder 패턴 사용
//    public Item(String name, String topNotes, String brand, String season, String perfumer) {
//        this.name = name;
//        this.brand = brand;
//        this.topNotes = topNotes;
//        this.season = season;
//        this.perfumer = perfumer;
//    }


}