package com.example.users.dataFetcher;

import com.example.users.codegen.client.*;
import com.example.users.codegen.types.CreateUserInput;
import com.example.users.codegen.types.UpdateUserInput;
import com.example.users.codegen.types.User;
import com.example.users.services.UsersService;
import com.example.users.testUtils.UserGenerator;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {DgsAutoConfiguration.class, UsersDataFetcher.class})
public class UsersDataFetcherTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @MockBean
    UsersService mockUsersService;

    @Test
    public void whenCallUsersQueryExpectGetAllUsers() {
        var fakeUsers = List.of(UserGenerator.generateDTOUser(), UserGenerator.generateDTOUser());
        Mockito.when(mockUsersService.getAllUsers()).thenReturn(fakeUsers);

        var request = new GraphQLQueryRequest(UsersGraphQLQuery.newRequest().build(),
                new UsersProjectionRoot().id().name());

        List<String> data = dgsQueryExecutor.executeAndExtractJsonPath(request.serialize(), "data.users[*].id");

        assertThat(data).hasSameElementsAs(fakeUsers.stream().map(User::getId).toList());
    }

    @Test
    public void whenCallUserQueryExpectGetCorrectUser() {
        var fakeUser = UserGenerator.generateDTOUser();
        Mockito.when(mockUsersService.getUserById(fakeUser.getId())).thenReturn(fakeUser);

        var request = new GraphQLQueryRequest(UserGraphQLQuery.newRequest().id(fakeUser.getId()).build(),
                new UserProjectionRoot().id().name());

        String data = dgsQueryExecutor.executeAndExtractJsonPath(request.serialize(), "data.user.id");

        assertThat(data).isEqualTo(fakeUser.getId());
    }

    @Test
    public void whenCallCreateUserMutationExpectCorrectUser() {
        var fakeUser = UserGenerator.generateDTOUser();
        Mockito.when(mockUsersService.createUser(fakeUser.getName(), fakeUser.getEmail())).thenReturn(fakeUser);

        var request =
                new GraphQLQueryRequest(CreateUserGraphQLQuery.newRequest()
                        .input(CreateUserInput.newBuilder().name(fakeUser.getName()).email(fakeUser.getEmail()).build())
                        .build(),
                        new CreateUserProjectionRoot().id());

        String data = dgsQueryExecutor.executeAndExtractJsonPath(request.serialize(), "data.createUser.id");

        assertThat(data).isEqualTo(fakeUser.getId());
    }

    @Test
    public void whenCallUpdateUserMutationExpectCorrectUser() {
        var fakeUser = UserGenerator.generateDTOUser();
        Mockito.when(mockUsersService.updateUser(fakeUser.getId(), fakeUser.getName(), null)).thenReturn(fakeUser);

        var request =
                new GraphQLQueryRequest(UpdateUserGraphQLQuery.newRequest()
                        .id(fakeUser.getId())
                        .input(UpdateUserInput.newBuilder().name(fakeUser.getName()).build())
                        .build(),
                        new UpdateUserProjectionRoot().id());

        String data = dgsQueryExecutor.executeAndExtractJsonPath(request.serialize(), "data.updateUser.id");

        assertThat(data).isEqualTo(fakeUser.getId());
    }

    @Test
    public void whenCallDeleteUserMutationExpectCorrectUser() {
        var fakeUser = UserGenerator.generateDTOUser();
        Mockito.when(mockUsersService.deleteUserById(fakeUser.getId())).thenReturn(fakeUser);

        var request =
                new GraphQLQueryRequest(DeleteUserGraphQLQuery.newRequest()
                        .id(fakeUser.getId())
                        .build(),
                        new DeleteUserProjectionRoot().id());

        String data = dgsQueryExecutor.executeAndExtractJsonPath(request.serialize(), "data.deleteUser.id");

        assertThat(data).isEqualTo(fakeUser.getId());
    }
}
