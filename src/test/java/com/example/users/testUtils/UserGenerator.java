package com.example.users.testUtils;

import com.example.test.type.CreateUserInput;
import com.example.users.codegen.types.User;

public class UserGenerator {

    private static int userCounter = 0;

    public static User generateDTOUser() {
        var id = getNextUserId();
        return User.newBuilder()
                .id(id)
                .name("User" + id)
                .email("user" + id + "@email.com").build();
    }

    public static CreateUserInput generateApolloCreateUserInput() {
        var id = getNextUserId();
        return CreateUserInput.builder()
                .name("User" + id)
                .email("user" + id + "@email.com").build();
    }

    private static String getNextUserId() {
        userCounter++;
        return Integer.toString(userCounter);
    }
}
