package com.example.users.db.repositories;

import com.example.users.db.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UsersRepository extends CrudRepository<User, UUID> {
}
