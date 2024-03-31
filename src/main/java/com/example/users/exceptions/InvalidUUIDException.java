package com.example.users.exceptions;

import com.netflix.graphql.types.errors.ErrorType;

public class InvalidUUIDException extends GraphqlException {
    public InvalidUUIDException(String invalidUUID) {
        super("Invalid UUID: " + invalidUUID);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.BAD_REQUEST;
    }
}
