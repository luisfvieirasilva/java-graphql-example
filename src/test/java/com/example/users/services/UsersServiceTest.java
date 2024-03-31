package com.example.users.services;

import com.example.users.dataFetcher.converters.DTOFromToEntity;
import com.example.users.db.entities.User;
import com.example.users.db.repositories.UsersRepository;
import com.example.users.exceptions.InvalidUUIDException;
import com.example.users.exceptions.UserNotFoundException;
import com.example.users.testUtils.UserGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    private UsersService usersService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        usersService = new UsersService(usersRepository);
    }

    @Test
    public void whenGetAllUsersExpectCorrectUsers() {
        User user1 = UserGenerator.generateDBUser();
        User user2 = UserGenerator.generateDBUser();
        when(usersRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        var users = usersService.getAllUsers();

        assertThat(users).containsExactlyInAnyOrder(DTOFromToEntity.toDTO(user1), DTOFromToEntity.toDTO(user2));
    }

    @Test
    public void whenGetUserByIdWithInvalidUUIDExpectInvalidUUIDException() {
        assertThrows(InvalidUUIDException.class, () -> usersService.getUserById("invalidUUID"));
    }

    @Test
    public void whenGetUserByIdNotFoundExpectUserNotFoundException() {
        when(usersRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> usersService.getUserById(UUID.randomUUID().toString()));
    }

    @Test
    public void givenUserWhenGetUserByIdExpectCorrectUser() {
        User user = UserGenerator.generateDBUser();
        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));

        var ret = usersService.getUserById(user.getId().toString());

        assertThat(ret).isEqualTo(DTOFromToEntity.toDTO(user));
    }

    @Test
    public void whenCreateUserExpectCorrectUser() {
        User user = UserGenerator.generateDBUser();
        when(usersRepository.save(
                argThat(userArg -> Objects.equals(userArg.getName(), user.getName()) && Objects.equals(
                        userArg.getEmail(), user.getEmail())))).thenReturn(user);

        var result = usersService.createUser(user.getName(), user.getEmail());

        assertThat(result).isEqualTo(DTOFromToEntity.toDTO(user));
    }

    @Test
    public void whenUpdateUserWithInvalidUUIDExpectInvalidUUIDException() {
        assertThrows(InvalidUUIDException.class, () -> usersService.updateUser("invalidUUID", "name", "email"));
    }

    @Test
    public void whenUpdateUserNotFoundExpectUserNotFoundException() {
        when(usersRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> usersService.updateUser(UUID.randomUUID().toString(), "name",
                "email"));
    }

    @Test
    public void givenUserWhenUpdateUserChangingNameExpectCorrectUser() {
        User user = UserGenerator.generateDBUser();
        User updatedUser = copyUser(user);
        updatedUser.setName("newName");
        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(usersRepository.save(updatedUser)).thenReturn(updatedUser);

        var result = usersService.updateUser(user.getId().toString(), updatedUser.getName(), null);

        assertThat(result).isEqualTo(DTOFromToEntity.toDTO(updatedUser));
    }

    @Test
    public void givenUserWhenUpdateUserChangingEmailExpectCorrectUser() {
        User user = UserGenerator.generateDBUser();
        User updatedUser = copyUser(user);
        updatedUser.setEmail("newEmail@email.com");
        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(usersRepository.save(updatedUser)).thenReturn(updatedUser);

        var result = usersService.updateUser(user.getId().toString(), null, updatedUser.getEmail());

        assertThat(result).isEqualTo(DTOFromToEntity.toDTO(updatedUser));
    }

    @Test
    public void givenUserWhenUpdateUserChangingBothNameAndEmailExpectCorrectUser() {
        User user = UserGenerator.generateDBUser();
        User updatedUser = copyUser(user);
        updatedUser.setName("newName");
        updatedUser.setEmail("newEmail@email.com");
        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(usersRepository.save(updatedUser)).thenReturn(updatedUser);

        var result = usersService.updateUser(user.getId().toString(), updatedUser.getName(), updatedUser.getEmail());

        assertThat(result).isEqualTo(DTOFromToEntity.toDTO(updatedUser));
    }

    @Test
    public void whenDeleteUserByIdWithInvalidUUIDExpectInvalidUUIDException() {
        assertThrows(InvalidUUIDException.class, () -> usersService.deleteUserById("invalidUUID"));
    }

    @Test
    public void whenDeleteUserByIdNotFoundExpectUserNotFoundException() {
        when(usersRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> usersService.updateUser(UUID.randomUUID().toString(), "name",
                "email"));
    }

    @Test
    public void givenUserWhenDeleteUserByIdExpectCorrectUser() {
        User user = UserGenerator.generateDBUser();
        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));

        var result = usersService.deleteUserById(user.getId().toString());

        verify(usersRepository).deleteById(user.getId());
        assertThat(result).isEqualTo(DTOFromToEntity.toDTO(user));
    }

    private User copyUser(User user) {
        return new User(user.getId(), user.getName(), user.getEmail());
    }
}
