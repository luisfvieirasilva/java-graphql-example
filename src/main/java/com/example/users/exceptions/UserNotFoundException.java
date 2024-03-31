package com.example.users.exceptions;

public class UserNotFoundException extends GraphqlException {
    public UserNotFoundException(String userId) {
        super(ErrorCode.USER_NOT_FOUND, "User not found: " + userId);
    }
}
