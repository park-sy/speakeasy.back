package com.speakeasy.domain.perfume;

import com.speakeasy.domain.User;
import lombok.*;

import javax.persistence.*;

@Builder
@Entity  //데이터베이스에 사용될 entity를 정의
@Getter  //getter 자동 선언
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)  //인자없는 생성자를 자동 생성
public class PerfumeScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "score_id")
    private Score score;

    private String name;
    private Integer point;
    public void edit(Integer point) {
        this.point = point;
    }
}
