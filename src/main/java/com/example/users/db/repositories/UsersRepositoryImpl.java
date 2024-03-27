package com.example.users.db.repositories;

import com.example.users.db.entities.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class UsersRepositoryImpl implements UsersRepository {

    final HashMap<UUID, User> userById = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return userById.values();
    }

    @Override
    public User getUserById(String id) {
        var user = userById.get(UUID.fromString(id));
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        return user;
    }

    @Override
    public User createUser(String name, String email) {
        User user = new User(name, email);
        userById.put(user.getId(), user);
        return user;
    }

    @Override
    public void updateUser(User user) {
        if (!userById.containsKey(user.getId())) {
            throw new IllegalArgumentException("User does not exist");
        }
        userById.put(user.getId(), user);
    }

    @Override
    public void deleteUser(UUID id) {
        userById.remove(id);
    }
}