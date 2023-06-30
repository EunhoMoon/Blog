package com.blog.exception;

public class InvalidSignInInformation extends BlogException {

    private static final String MESSAGE = "아이디/비밀번호를 확인해주세요";

    public InvalidSignInInformation() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }

}
