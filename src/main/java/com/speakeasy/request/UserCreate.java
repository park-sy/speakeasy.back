package com.speakeasy.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class UserCreate {
    @NotBlank(message = "아이디를 입력해주세요")
    private String uid;

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @Builder
    public UserCreate(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }
}
