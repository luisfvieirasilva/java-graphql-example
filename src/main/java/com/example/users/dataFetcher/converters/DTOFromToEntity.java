package com.example.users.dataFetcher.converters;

public class DTOFromToEntity {

    public static com.example.users.codegen.types.User toDTO(com.example.users.db.entities.User user) {
        return new com.example.users.codegen.types.User(user.getId().toString(), user.getName(), user.getEmail());
    }
}
