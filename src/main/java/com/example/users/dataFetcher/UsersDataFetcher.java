package com.example.users.dataFetcher;

import com.example.users.codegen.types.CreateUserInput;
import com.example.users.codegen.types.UpdateUserInput;
import com.example.users.codegen.types.User;
import com.example.users.dataFetcher.converters.DTOFromToEntity;
import com.example.users.db.repositories.UsersRepository;
import com.example.users.db.repositories.UsersRepositoryImpl;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.Collection;
import java.util.stream.Collectors;

@DgsComponent
public class UsersDataFetcher {

    private final UsersRepository usersRepository = new UsersRepositoryImpl();

    @DgsQuery
    public Collection<User> users() {
        return usersRepository.getAllUsers().stream().map(DTOFromToEntity::toDTO).collect(Collectors.toList());
    }

    @DgsQuery
    public User user(@InputArgument String id) {
        return DTOFromToEntity.toDTO(usersRepository.getUserById(id));
    }

    @DgsMutation
    public User createUser(@InputArgument CreateUserInput input) {
        return DTOFromToEntity.toDTO(usersRepository.createUser(input.getName(), input.getEmail()));
    }

    @DgsMutation
    public User updateUser(@InputArgument String id, @InputArgument UpdateUserInput input) {
        var user = usersRepository.getUserById(id);
        if (input.getName() != null) {
            user.setName(input.getName());
        }
        if (input.getEmail() != null) {
            user.setEmail(input.getEmail());
        }
        usersRepository.updateUser(user);

        return DTOFromToEntity.toDTO(user);
    }

    @DgsMutation
    public User deleteUser(@InputArgument String id) {
        var user = usersRepository.getUserById(id);
        usersRepository.deleteUser(user.getId());
        return DTOFromToEntity.toDTO(user);
    }
}
