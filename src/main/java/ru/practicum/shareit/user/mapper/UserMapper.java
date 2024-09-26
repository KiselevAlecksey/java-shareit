package ru.practicum.shareit.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.model.User;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {

    public User mapToUser(UserCreateDto request) {
        return User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .build();
    }

    public User mapToUser(UserUpdateDto request) {
        return User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .build();
    }

    public UserDtoResponse mapToUserDto(User user) {
        return new UserDtoResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public User updateUserFields(User user, UserUpdateDto request) {

        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }
        if (request.hasName()) {
            user.setName(request.getName());
        }
        return user;
    }
}
