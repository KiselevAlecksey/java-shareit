package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InMemUserRepository implements UserRepository {
    private long countId = 0;
    private final Map<Long, User> userMap;

    public Collection<User> findAll() {
        return userMap.values().stream().toList();
    }

    public Optional<User> getById(Long id) {

        return getUser(id);
    }

    public Optional<User> create(User user) {
        countId++;
        user.setId(countId);
        userMap.put(countId, user);
        return getUser(user.getId());
    }

    public Optional<User> update(User user) {

        userMap.put(user.getId(), user);

        return getUser(user.getId());
    }

    public boolean remove(Long id) {
        return userMap.remove(id) != null;
    }

    private Optional<User> getUser(Long userId) {
        User user =  userMap.get(userId);

        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        return Optional.of(user);
    }
}
