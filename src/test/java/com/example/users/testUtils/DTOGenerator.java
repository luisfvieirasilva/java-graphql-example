package com.example.users.testUtils;

import com.example.users.codegen.types.User;

public class DTOGenerator {

    private static int userCounter = 0;

    public static User generateFakeUser() {
        userCounter++;
        return User.newBuilder()
                .id(Integer.toString(userCounter))
                .name("User" + userCounter)
                .email("user" + userCounter + "@email.com").build();
    }
}
