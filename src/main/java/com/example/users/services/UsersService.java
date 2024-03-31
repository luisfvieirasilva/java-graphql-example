package com.example.users.services;

import com.example.users.codegen.types.User;
import com.example.users.dataFetcher.converters.DTOFromToEntity;
import com.example.users.db.repositories.UsersDbRepository;
import com.example.users.exceptions.InvalidUUIDException;
import com.example.users.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UsersService {

    private final UsersDbRepository usersRepository;

    public UsersService(UsersDbRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<User> getAllUsers() {
        return StreamSupport.stream(usersRepository.findAll().spliterator(), false).map(DTOFromToEntity::toDTO)
                .collect(Collectors.toList());
    }

    public User getUserById(String id) {
        var user = usersRepository.findById(convertToUUID(id));
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        return DTOFromToEntity.toDTO(user.get());
    }

    public User createUser(String name, String email) {
        var newUser = new com.example.users.db.entities.User(name, email);
        newUser = usersRepository.save(newUser);

        return DTOFromToEntity.toDTO(newUser);
    }

    public User updateUser(String id, String name, String email) {
        var optionalUser = usersRepository.findById(convertToUUID(id));
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        var user = optionalUser.get();

        if (name != null) {
            user.setName(name);
        }
        if (email != null) {
            user.setEmail(email);
        }
        user = usersRepository.save(user);

        return DTOFromToEntity.toDTO(user);
    }

    public User deleteUserById(String id) {
        var user = this.getUserById(id);
        usersRepository.deleteById(UUID.fromString(id));
        return user;
    }

    private UUID convertToUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDException(id);
        }
    }
}
