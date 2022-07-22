package com.speakeasy.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserSignIn {
    @NotBlank(message = "아이디를 입력해주세요")
    private String uid;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
    @Builder
    public UserSignIn(String uid, String password) {
        this.uid = uid;
        this.password = password;
    }
}

