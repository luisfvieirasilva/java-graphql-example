package com.example.users.dataFetcher;

import com.example.users.exceptions.GraphqlException;
import com.netflix.graphql.dgs.exceptions.DefaultDataFetcherExceptionHandler;
import com.netflix.graphql.types.errors.TypedGraphQLError;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class CustomDataFetchingExceptionHandler  extends DefaultDataFetcherExceptionHandler {
    @NotNull
    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(DataFetcherExceptionHandlerParameters handlerParameters) {

        if (handlerParameters.getException() instanceof GraphqlException e) {

            var graphqlError = TypedGraphQLError.newBuilder()
                    .errorType(e.getErrorType())
                    .message(e.getMessage())
                    .path(handlerParameters.getPath())
                    .build();

            var result = DataFetcherExceptionHandlerResult.newResult()
                    .error(graphqlError)
                    .build();

            return CompletableFuture.completedFuture(result);
        }

        return super.handleException(handlerParameters);
    }
}
