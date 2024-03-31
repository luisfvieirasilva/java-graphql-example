package com.example.users.exceptions;

import com.netflix.graphql.types.errors.ErrorType;

public abstract class GraphqlException extends RuntimeException {

    private final ErrorCode errorCode;

    public GraphqlException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorType getErrorType() {
        return errorCode.getErrorType();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
