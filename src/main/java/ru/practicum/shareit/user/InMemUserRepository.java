package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InMemUserRepository implements UserRepository {
    long countId = 0;

    final Map<Long, User> userMap = new HashMap<>();

    public List<User> findAll() {
        return userMap.values().stream().toList();
    }

    public Optional<User> getById(long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    public User create(User user) {
        countId++;
        user.setId(countId);
        userMap.put(countId, user);
        return getById(user.getId()).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public User update(User user) {

        userMap.put(user.getId(), user);

        return getById(user.getId()).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public boolean remove(long id) {
        return userMap.remove(id) != null;
    }
}
