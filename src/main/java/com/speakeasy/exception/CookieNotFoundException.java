package com.speakeasy.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CookieNotFoundException extends RuntimeException {

    public CookieNotFoundException(String msg) {
        super(msg);
    }
    public CookieNotFoundException() {
        super();
    }
}