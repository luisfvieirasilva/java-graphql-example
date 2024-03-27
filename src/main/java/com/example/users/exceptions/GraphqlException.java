package com.example.users.exceptions;

import com.netflix.graphql.types.errors.ErrorType;

public abstract class GraphqlException  extends RuntimeException {

    public GraphqlException(String message) {
        super(message);
    }

   public abstract ErrorType getErrorType();
}
