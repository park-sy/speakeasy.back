package com.speakeasy.domain.perfume;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Builder
@Entity  //데이터베이스에 사용될 entity를 정의
@Getter
@Subselect("select perfume_id, count(*) / count(distinct note_id) as value from perfume_type_note " +
        "group by perfume_id")
@Synchronize("perfume_type_note")
public class PerfumeTypeSub {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "perfume_id")
    private Long perfume_id;
    @Column(name = "value")
    private double flag;
}
