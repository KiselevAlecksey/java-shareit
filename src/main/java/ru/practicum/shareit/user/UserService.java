package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> findAll();

    UserResponseDto getById(long id);

    UserResponseDto create(UserCreateDto userRequest);

    UserResponseDto update(UserUpdateDto userRequest);

    void remove(long id);
}
