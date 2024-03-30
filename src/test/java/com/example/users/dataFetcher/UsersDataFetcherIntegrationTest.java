package com.example.users.dataFetcher;

import com.example.test.*;
import com.example.test.fragment.UserFragment;
import com.example.test.type.CreateUserInput;
import com.example.test.type.UpdateUserInput;
import com.example.users.db.repositories.UsersRepository;
import com.example.users.testUtils.SyncApolloClient;
import com.example.users.testUtils.UserGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.ArrayList;
import java.util.List;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersDataFetcherIntegrationTest {

    private final UsersRepository usersRepository;
    private final SyncApolloClient syncClient;

    public UsersDataFetcherIntegrationTest(@Autowired UsersRepository usersRepository, @LocalServerPort Integer port) {
        this.usersRepository = usersRepository;
        syncClient = new SyncApolloClient.Builder().serverUrl("http://localhost:" + port + "/graphql").build();
    }

    @BeforeEach
    public void setUp() {
        usersRepository.clear();
    }

    @Test
    public void GivenUsersAlreadyCreatedWhenCallUsersQueryExpectGetAllUsers() {
        var users = populateUsers(3);

        var response = syncClient.query(new UsersQuery());

        var responseUsers = response.dataOrThrow().users.stream().map(user -> user.userFragment).toList();

        assertThat(responseUsers).containsExactlyInAnyOrderElementsOf(users);
    }

    @Test
    public void GivenNoUsersWhenCallUsersQueryExpectEmptyList() {
        var response = syncClient.query(new UsersQuery());

        assertThat(response.dataOrThrow().users).isEmpty();
    }

    @Test
    public void WhenCallUserQueryIfInvalidIdExpectNotFoundError() {
        var response = syncClient.query(new UserQuery("invalidId"));

        assertThat(response.dataOrThrow().user).isNull();
        assertThat(response.errors).isNotEmpty();
        assertThat(response.errors.get(0).getExtensions()).contains(entry("errorType", "NOT_FOUND"));
    }

    @Test
    public void GivenUserWhenCallUserQueryIfItsIdExpectGetCorrectUser() {
        var user = populateUsers(1).get(0);

        var response = syncClient.query(new UserQuery(user.id));

        assertThat(response.dataOrThrow().user.userFragment).isEqualTo(user);
    }

    @Test
    public void whenCallCreateUserMutationExpectCorrectUser() {
        var newUser = UserGenerator.generateDTOUser();

        var responseUser =
                syncClient.mutate(new CreateUserMutation(
                                CreateUserInput.builder().name(newUser.getName()).email(newUser.getEmail()).build()))
                        .dataOrThrow().createUser.userFragment;
        
        //noinspection AssertBetweenInconvertibleTypes
        assertThat(responseUser).usingRecursiveComparison().ignoringFieldsMatchingRegexes("\\$.*").ignoringFields("id")
                .isEqualTo(newUser);
    }

    @Test
    public void WhenCallUpdateUserMutationIfInvalidIdExpectNotFoundError() {
        var response = syncClient.mutate(new UpdateUserMutation("invalidId", UpdateUserInput.builder().name(
                "newName").build()));

        assertThat(response.data).isNull();
        assertThat(response.errors).isNotEmpty();
        assertThat(response.errors.get(0).getExtensions()).contains(entry("errorType", "NOT_FOUND"));
    }

    @Test
    public void GivenUserWhenCallUpdateUserMutationWithNameExpectUpdatedName() {
        var user = populateUsers(1).get(0);
        var newName = "newName";

        var responseUser =
                syncClient.mutate(new UpdateUserMutation(user.id, UpdateUserInput.builder().name(newName).build()))
                        .dataOrThrow().updateUser.userFragment;

        assertThat(responseUser).usingRecursiveComparison().ignoringFieldsMatchingRegexes("\\$.*")
                .ignoringFields("name")
                .isEqualTo(user);
        assertThat(responseUser.name).isEqualTo(newName);
    }

    @Test
    public void GivenUserWhenCallUpdateUserMutationWithEmailExpectUpdatedEmail() {
        var user = populateUsers(1).get(0);
        var newEmail = "newEmail@email.com";

        var responseUser =
                syncClient.mutate(new UpdateUserMutation(user.id, UpdateUserInput.builder().email(newEmail).build()))
                        .dataOrThrow().updateUser.userFragment;

        assertThat(responseUser).usingRecursiveComparison().ignoringFieldsMatchingRegexes("\\$.*")
                .ignoringFields("email")
                .isEqualTo(user);
        assertThat(responseUser.email).isEqualTo(newEmail);
    }

    @Test
    public void WhenCallDeleteUserMutationIfInvalidIdExpectNotFoundError() {
        var response = syncClient.mutate(new DeleteUserMutation("invalidId"));

        assertThat(response.data).isNull();
        assertThat(response.errors).isNotEmpty();
        assertThat(response.errors.get(0).getExtensions()).contains(entry("errorType", "NOT_FOUND"));
    }

    @Test
    public void GivenUserWhenCallDeleteUserMutationExpectCorrectUser() {
        var user = populateUsers(1).get(0);

        var responseUser = syncClient.mutate(new DeleteUserMutation(user.id)).dataOrThrow().deleteUser.userFragment;

        assertThat(responseUser).isEqualTo(user);
    }

    @Test
    public void GivenDeletedUserWhenCallUsersQueryExpectUserNotInList() {
        var user = populateUsers(3).get(0);

        syncClient.mutate(new DeleteUserMutation(user.id));

        var responseUserIds =
                syncClient.query(new UsersQuery()).dataOrThrow().users.stream().map(user1 -> user1.userFragment.id)
                        .toList();

        assertThat(responseUserIds).hasSize(2);
        assertThat(responseUserIds).doesNotContain(user.id);
    }

    private List<UserFragment> populateUsers(int numberOfUsers) {
        var createUserInputs = new ArrayList<CreateUserInput>();
        for (int i = 0; i < numberOfUsers; i++) {
            createUserInputs.add(UserGenerator.generateApolloCreateUserInput());
        }

        return createUserInputs.stream()
                .map(input -> this.syncClient.mutate(new CreateUserMutation(input))
                        .dataOrThrow().createUser.userFragment).toList();
    }
}
