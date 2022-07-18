package com.speakeasy.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.max;
import static java.lang.Math.min;

//페이지서치를 위한 클래스 구현
@Getter //getter, setter 사용
@Setter
@Builder //빌더패턴 사용
public class ItemSearch {

    private static final int MAX_SIZE = 2000;

    @Builder.Default //인자가 없을 시 1페이지 로드
    private Integer page = 1;
    @Builder.Default //인자가 없을 시 페이지당 10개의 아이템 로드
    private Integer size = 10;

    public long getOffset(){
        //페이지 0이 요청될 시 1페이지 반환
        return (long) (max(1,page) - 1) * min(size,MAX_SIZE);
    }
}
