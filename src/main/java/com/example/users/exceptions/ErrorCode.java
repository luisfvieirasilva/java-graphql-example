package com.example.users.exceptions;

import com.netflix.graphql.types.errors.ErrorType;

public enum ErrorCode {
    USER_NOT_FOUND(ErrorType.NOT_FOUND),
    INVALID_UUID(ErrorType.BAD_REQUEST);

    private final ErrorType errorType;

    ErrorCode(ErrorType errorType) {
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
