package com.speakeasy.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class UserSignUp {
    @NotBlank(message = "아이디를 입력해주세요")
    private String email;

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @Builder
    public UserSignUp(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}