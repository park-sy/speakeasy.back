package com.speakeasy.response.perfume;

import com.speakeasy.domain.perfume.Perfume;
import lombok.Getter;

//응답 처리를 위한 클래스 생성
@Getter
public class PerfumeResponse {

    private final Long id;
    private final String name;
    private final String brand;
    private final Double point;

//    @Builder //빌더 패턴 새용
    public PerfumeResponse(Long id, String name, String brand, Double point) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.point = point;
    }

    public PerfumeResponse(Perfume perfume) {
        this.id = perfume.getId();
        this.name = perfume.getName();
        this.brand = perfume.getBrand();
        this.point = (double) perfume.getPoints();
    }

}
