package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    Collection<User> findAll();

    Optional<User> getById(Long id);

    Optional<User> create(User user);

    Optional<User> update(User user);

    boolean remove(Long id);
}
