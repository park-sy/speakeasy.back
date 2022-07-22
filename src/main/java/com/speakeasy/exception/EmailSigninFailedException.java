package com.speakeasy.exception;

public class EmailSigninFailedException extends RuntimeException {
    public EmailSigninFailedException(String msg, Throwable t) {
        super(msg, t);
    }

    public EmailSigninFailedException(String msg) {
        super(msg);
    }

    public EmailSigninFailedException() {
        super(
                "로그인 정보가 잘못 되었습니다."
        );
    }
}