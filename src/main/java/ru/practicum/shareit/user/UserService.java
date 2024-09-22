package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> findAll();

    UserDto getById(long id);

    UserDto create(NewUserRequest userRequest);

    UserDto update(UpdateUserRequest userRequest, Long id);

    boolean remove(long id);
}
