package com.speakeasy.domain;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.HashMap;
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
    private Integer year;
    private Integer gender;
    @ManyToMany
    @JoinTable(name = "item_top_notes"
            ,joinColumns = @JoinColumn(name = "item_id")
            ,inverseJoinColumns = @JoinColumn(name = "note_id"))
    private Set<Note> topNotes;

    @ManyToMany
    @JoinTable(name = "item_heart_notes"
            ,joinColumns = @JoinColumn(name = "item_id")
            ,inverseJoinColumns = @JoinColumn(name = "note_id"))
    private Set<Note> heartNotes;

    @ManyToMany
    @JoinTable(name = "item_base_notes"
            ,joinColumns = @JoinColumn(name = "item_id")
            ,inverseJoinColumns = @JoinColumn(name = "note_id"))
    private Set<Note> baseNotes;

    @Builder.Default
    private Long ratingPoints = 0L;
    @Builder.Default
    private Long scentPoints= 0L;
    @Builder.Default
    private Long longevityPoints= 0L;
    @Builder.Default
    private Long sillagePoints= 0L;
    @Builder.Default
    private Long bottlePoints= 0L;
    @Builder.Default
    private Long valueOfMoneyPoints= 0L;
    @Builder.Default
    private Long votes= 0L;

    @Column(columnDefinition = "json")
    @Type(type = "json")
    private HashMap<String,Long> type;
    @Column(columnDefinition = "json")
    @Type(type = "json")
    private HashMap<String,Long> season;
    @Column(columnDefinition = "json")
    @Type(type = "json")
    private HashMap<String,Long> occasion;
    @Column(columnDefinition = "json")
    @Type(type = "json")
    private HashMap<String,Long> audience;

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