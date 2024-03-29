package com.example.users.dataFetcher;

import com.example.users.codegen.types.CreateUserInput;
import com.example.users.codegen.types.UpdateUserInput;
import com.example.users.codegen.types.User;
import com.example.users.services.UsersService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.Collection;

@DgsComponent
public class UsersDataFetcher {

    private final UsersService usersService;

    public UsersDataFetcher(UsersService usersService) {
        this.usersService = usersService;
    }

    @DgsQuery
    public Collection<User> users() {
        return usersService.getAllUsers();
    }

    @DgsQuery
    public User user(@InputArgument String id) {
        return usersService.getUserById(id);
    }

    @DgsMutation
    public User createUser(@InputArgument CreateUserInput input) {
        return usersService.createUser(input.getName(), input.getEmail());
    }

    @DgsMutation
    public User updateUser(@InputArgument String id, @InputArgument UpdateUserInput input) {
        return usersService.updateUser(id, input.getName(), input.getEmail());
    }

    @DgsMutation
    public User deleteUser(@InputArgument String id) {
        return usersService.deleteUserById(id);
    }
}
