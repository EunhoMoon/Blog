package com.blog.exception;

import lombok.Getter;

/**
 * status = 400
 */
@Getter
public class InvalidRequest extends BlogException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(Throwable cause) {
        super(MESSAGE, cause);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }

}
