package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.util.List;

public interface UserService {
    List<UserDtoResponse> findAll();

    UserDtoResponse getById(long id);

    UserDtoResponse create(UserCreateDto userRequest);

    UserDtoResponse update(UserUpdateDto userRequest);

    boolean remove(long id);
}
