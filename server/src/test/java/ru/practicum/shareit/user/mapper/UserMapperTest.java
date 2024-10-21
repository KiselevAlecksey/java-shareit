package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.util.TestDataFactory.TEST_USER_ID;

@DisplayName("UserMapper")
class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    @DisplayName("Должен маппить поля из дто в модель")
    void should_map_to_user() {
        UserCreateDto createDto = new UserCreateDto();
        createDto.setEmail("user@example.com");
        createDto.setName("Имя пользователя");

        User user = userMapper.mapToUser(createDto);

        assertEquals("user@example.com", user.getEmail(), "Email должен совпадать");
        assertEquals("Имя пользователя", user.getName(), "Имя должно совпадать");
    }

    @Test
    @DisplayName("Должен маппить поля из модели в дто")
    void should_map_to_user_dto() {
        User user = new User();
        user.setId(TEST_USER_ID);
        user.setEmail("user@example.com");
        user.setName("Имя пользователя");

        UserResponseDto userDto = userMapper.mapToUserDto(user);

        assertEquals(TEST_USER_ID, userDto.id(), "ID должен совпадать");
        assertEquals("user@example.com", userDto.email(), "Email должен совпадать");
        assertEquals("Имя пользователя", userDto.name(), "Имя должно совпадать");
    }

    @Test
    @DisplayName("Должен обновить поля из дто в модель")
    void should_update_user_fields() {
        User user = new User();
        user.setEmail("old_email@example.com");
        user.setName("Старое имя");

        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setEmail("new_email@example.com");
        updateDto.setName("Новое имя");

        User updatedUser = userMapper.updateUserFields(user, updateDto);

        assertEquals("new_email@example.com", updatedUser.getEmail(), "Email должен обновиться");
        assertEquals("Новое имя", updatedUser.getName(), "Имя должно обновиться");
    }

    @Test
    @DisplayName("Должен не обновлять поля из дто в модель")
    void should_not_update_fields_if_not_provided() {
        User user = new User();
        user.setEmail("old_email@example.com");
        user.setName("Старое имя");

        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setEmail(null);
        updateDto.setName(null);

        User updatedUser = userMapper.updateUserFields(user, updateDto);

        assertEquals("old_email@example.com", updatedUser.getEmail(), "Email не должен измениться");
        assertEquals("Старое имя", updatedUser.getName(), "Имя не должно измениться");
    }
}