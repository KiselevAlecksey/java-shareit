package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> findAll();

    UserDto getById(long id);

    UserDto create(UserDto userRequest);

    UserDto update(UserDto userRequest, Long id);

    boolean remove(long id);
}
