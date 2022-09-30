package com.speakeasy.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class KakaoSignIn {
    @NotBlank(message = "아이디를 입력해주세요")
    private String email;

    @NotBlank(message = "소셜 제공자를 입력해주세요")
    private String provider;


    @Builder
    public KakaoSignIn(String email, String provider) {
        this.email = email;
        this.provider = provider;
    }
}