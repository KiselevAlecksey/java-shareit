package ru.practicum.shareit.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {

    public static User mapToUser(NewUserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .build();
    }

    public static User mapToUser(UpdateUserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .build();
    }

    public static UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public static User updateUserFields(User user, UpdateUserRequest request) {

        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }
        if (request.hasName()) {
            user.setName(request.getName());
        }
        return user;
    }
}
