package com.speakeasy.domain.perfume;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Set;

@Builder
@Entity  //데이터베이스에 사용될 entity를 정의
@Getter  //getter 자동 선언
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)  //인자없는 생성자를 자동 생성
@TypeDef(name = "json", typeClass = JsonType.class)
public class Perfume {

    @Id  //PK임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //pk 설정을 DB에게 위임,
    @Column(name = "perfume_id")
    private Long id;
    private String name;
    private String brand;
    private Integer year;
    private Integer gender;
    @ManyToMany
    @JoinTable(name = "perfume_top_notes"
            ,joinColumns = @JoinColumn(name = "perfume_id")
            ,inverseJoinColumns = @JoinColumn(name = "note_id"))
    private Set<Note> topNotes;

    @ManyToMany
    @JoinTable(name = "perfume_heart_notes"
            ,joinColumns = @JoinColumn(name = "perfume_id")
            ,inverseJoinColumns = @JoinColumn(name = "note_id"))
    private Set<Note> heartNotes;

    @ManyToMany
    @JoinTable(name = "perfume_base_notes"
            ,joinColumns = @JoinColumn(name = "perfume_id")
            ,inverseJoinColumns = @JoinColumn(name = "note_id"))
    private Set<Note> baseNotes;


    private String description;
    private String perfumer;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;

    @OneToMany(mappedBy = "perfume", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc")
    private Set<PerfumeComment> comments;

    @OneToMany(mappedBy = "perfume", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc")
    private Set<PerfumeImages> images;

    @OneToMany(mappedBy = "perfume", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id asc")
    private Set<PerfumeTypeNote> type;

    private String season;
    private String occasion;
    private String audience;
    private Double points;
    private Long votes;

}