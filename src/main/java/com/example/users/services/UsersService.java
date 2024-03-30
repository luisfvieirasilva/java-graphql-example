package com.example.users.services;

import com.example.users.codegen.types.User;
import com.example.users.dataFetcher.converters.DTOFromToEntity;
import com.example.users.db.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<User> getAllUsers() {
        return usersRepository.getAllUsers().stream().map(DTOFromToEntity::toDTO).collect(Collectors.toList());
    }

    public User getUserById(String id) {
        return DTOFromToEntity.toDTO(usersRepository.getUserById(id));
    }

    public User createUser(String name, String email) {
        return DTOFromToEntity.toDTO(usersRepository.createUser(name, email));
    }

    public User updateUser(String id, String name, String email) {
        var user = usersRepository.getUserById(id);

        if (name != null) {
            user.setName(name);
        }
        if (email != null) {
            user.setEmail(email);
        }
        usersRepository.updateUser(user);

        return DTOFromToEntity.toDTO(user);
    }

    public User deleteUserById(String id) {
        var user = usersRepository.getUserById(id);
        usersRepository.deleteUser(user.getId().toString());
        return DTOFromToEntity.toDTO(user);
    }
}
