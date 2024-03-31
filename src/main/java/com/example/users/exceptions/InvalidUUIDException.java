package com.example.users.exceptions;

public class InvalidUUIDException extends GraphqlException {
    public InvalidUUIDException(String invalidUUID) {
        super(ErrorCode.INVALID_UUID, "Invalid UUID: " + invalidUUID);
    }
}
