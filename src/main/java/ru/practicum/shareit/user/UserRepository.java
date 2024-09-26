package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    Optional<User> getById(long id);

    User create(User user);

    User update(User user);

    boolean remove(long id);

    List<String> getEmails();
}
