package com.example.users.exceptions;

import com.netflix.graphql.types.errors.ErrorType;

public class NotFoundException extends GraphqlException {
    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.NOT_FOUND;
    }
}
