package com.example.users.testUtils;

import com.example.test.type.CreateUserInput;
import com.example.users.codegen.types.User;

import java.util.UUID;

public class UserGenerator {

    private static int userCounter = 0;

    public static User generateDTOUser() {
        var userCounter = getNextUserCounter();
        return User.newBuilder()
                .id(UUID.randomUUID().toString())
                .name("User" + userCounter)
                .email("user" + userCounter + "@email.com").build();
    }

    public static CreateUserInput generateApolloCreateUserInput() {
        var userCounter = getNextUserCounter();
        return CreateUserInput.builder()
                .name("User" + userCounter)
                .email("user" + userCounter + "@email.com").build();
    }

    public static com.example.users.db.entities.User generateDBUser() {
        var userCounter = getNextUserCounter();
        return new com.example.users.db.entities.User(UUID.randomUUID(), "User" + userCounter, "user" + userCounter +
                "@email.com");
    }

    private static String getNextUserCounter() {
        userCounter++;
        return Integer.toString(userCounter);
    }
}
