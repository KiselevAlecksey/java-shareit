package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.util.Collection;

public interface UserService {
    Collection<UserDtoResponse> findAll();

    UserDtoResponse getById(long id);

    UserDtoResponse create(UserDto userRequest);

    UserDtoResponse update(UserDto userRequest, Long id);

    boolean remove(long id);
}
