package com.speakeasy.exception;

public class ItemNotFound extends RuntimeException {

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public ItemNotFound() {
        super(MESSAGE);
    }
}
