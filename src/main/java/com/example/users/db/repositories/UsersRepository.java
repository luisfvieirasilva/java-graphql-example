package com.example.users.db.repositories;

import com.example.users.db.entities.User;

import java.util.*;

public interface UsersRepository {
    Collection<User> getAllUsers();

    User getUserById(String id);

    User createUser(String name, String email);

    void updateUser(User user);

    void deleteUser(UUID id);
}
