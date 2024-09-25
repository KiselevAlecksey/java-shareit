package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.NewUserDtoRequest;
import ru.practicum.shareit.user.dto.UpdateUserDtoRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.util.List;

public interface UserService {
    List<UserDtoResponse> findAll();

    UserDtoResponse getById(long id);

    UserDtoResponse create(NewUserDtoRequest userRequest);

    UserDtoResponse update(UpdateUserDtoRequest userRequest);

    boolean remove(long id);
}
