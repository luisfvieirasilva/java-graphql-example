package com.example.users.testUtils;

import com.apollographql.apollo3.api.ApolloResponse;
import com.apollographql.apollo3.api.Mutation;
import com.apollographql.apollo3.api.Query;
import com.apollographql.apollo3.runtime.java.ApolloClient;

import java.util.concurrent.CompletableFuture;

public class SyncApolloClient {

    private final ApolloClient apolloClient;

    public SyncApolloClient(ApolloClient apolloClient) {
        this.apolloClient = apolloClient;
    }

    public <T extends Mutation.Data> ApolloResponse<T> mutate(Mutation<T> mutation) {
        var futureReturn = new CompletableFuture<ApolloResponse<T>>();

        apolloClient.mutation(mutation).enqueue(futureReturn::complete);

        try {
            return futureReturn.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends Query.Data> ApolloResponse<T> query(Query<T> query) {
        var futureReturn = new CompletableFuture<ApolloResponse<T>>();

        apolloClient.query(query).enqueue(futureReturn::complete);

        try {
            return futureReturn.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class Builder {

        ApolloClient.Builder apolloClientBuilder = new ApolloClient.Builder();

        public Builder serverUrl(String serverUrl) {
            apolloClientBuilder.serverUrl(serverUrl);
            return this;
        }

        public SyncApolloClient build() {
            return new SyncApolloClient(apolloClientBuilder.build());
        }
    }
}
