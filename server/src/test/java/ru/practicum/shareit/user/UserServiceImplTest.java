package ru.practicum.shareit.user;


import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ParameterConflictException;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.util.TestDataFactory.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@DisplayName("UserService")
class UserServiceImplTest {
    private final EntityManager entityManager;

    private final UserService userService;

    @Test
    @DisplayName("Должен создавать пользователя")
    void should_create() {
        UserResponseDto responseDto = userService.create(createUserDto("test name", "test@email.com"));

        long testId = responseDto.id();

        assertEquals(createdUserDto(testId, "test name", "test@email.com"), responseDto, "Поля должны совпадать");
    }

    @Test
    @DisplayName("Должен вернуть всех пользователей")
    void should_find_all() {
        List<UserResponseDto> userResponseDtoList = userService.findAll();

        assertEquals(createUserDtoList(), userResponseDtoList, "Списки должны совпадать");
    }

    @Test
    @DisplayName("Должен вернуть пользователя по id")
    void should_get_by_id() {
        UserResponseDto responseDto = userService.getById(TEST_USER_ID);

        assertEquals(createdUserDto(), responseDto, "Дто должны совпадать");
    }

    @Test
    @DisplayName("Должен обновить пользователя")
    void should_update() {
        UserResponseDto responseDto = userService.update(updateUserDto(TEST_USER_ID, "test name", "ivan@example.com"));

        assertEquals(updatedUserDto(TEST_USER_ID, "test name", "ivan@example.com"), responseDto, "Поля должны совпадать");
    }

    @Test
    @DisplayName("Должен удалять пользователя и выбрасывать NotFoundException при получении по id")
    void should_remove() {
        userService.remove(TEST_USER_ID);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userService.getById(TEST_USER_ID);
        });

        assertEquals("Пользователь не найден", exception.getMessage(), "Сообщение исключения должно совпадать");
    }

    @Test
    @DisplayName("Должен не обновлять пользователя с занятым полем email")
    void should_not_update_email_conflict() {
        ParameterConflictException exception = assertThrows(ParameterConflictException.class, () -> {
            userService.update(updateUserDto(TEST_USER2_ID, "test name", "ivan@example.com"));
        });

        assertEquals("Некорректное значение параметра email: Тайкой email уже занят",
                "Некорректное значение параметра " + exception.getParameter() + ": "
                        + exception.getReason(), "Поля должны совпадать");
    }

    @Test
    @DisplayName("Должен обновлять пользователя с незанятым полем email")
    void should_update_email_not_conflict() {

        UserResponseDto dto = userService.update(updateUserDto(TEST_USER2_ID, "test name", "test@example.com"));

        assertEquals(updatedUserDto(TEST_USER2_ID, "test name", "test@example.com"), dto, "Поля должны совпадать");
    }

    @Test
    @DisplayName("Должен не обновлять пользователя с не существующим id")
    void should_not_update_not_exist_user() {

        long userId = 999L;

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userService.update(updateUserDto(userId, "test name", "test@example.com"));
        });

        assertEquals("Пользователь не найден", exception.getMessage(), "Сообщение исключения должно совпадать");
    }
}
