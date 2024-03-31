package com.example.users.db.repositories;

import com.example.users.db.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsersRepository extends CrudRepository<User, UUID> {
}
