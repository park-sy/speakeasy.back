package com.speakeasy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserExistException extends RuntimeException {

    public UserExistException(String msg) {
        super(msg);
    }
    public UserExistException() {
        super();
    }
}