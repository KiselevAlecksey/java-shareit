package ru.practicum.shareit.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.NewUserDtoRequest;
import ru.practicum.shareit.user.dto.UpdateUserDtoRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.model.User;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {

    public User mapToUser(NewUserDtoRequest request) {
        return User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .build();
    }

    public User mapToUser(UpdateUserDtoRequest request) {
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

    public User updateUserFields(User user, UpdateUserDtoRequest request) {

        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }
        if (request.hasName()) {
            user.setName(request.getName());
        }
        return user;
    }
}
