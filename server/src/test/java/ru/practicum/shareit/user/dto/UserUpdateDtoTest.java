package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserUpdateDto")
class UserUpdateDtoTest {

    @Test
    @DisplayName("Должен проверить поле name")
    void should_has_name() {
        UserUpdateDto dtoWithName = new UserUpdateDto();
        dtoWithName.setName("Имя пользователя");
        dtoWithName.isNotBlank(dtoWithName.getName());

        assertTrue(dtoWithName.hasName(), "hasName должен вернуть true, если имя установлено");

        UserUpdateDto dtoWithoutName = new UserUpdateDto();
        assertFalse(dtoWithoutName.hasName(), "hasName должен вернуть false, если имя не установлено");
    }

    @Test
    @DisplayName("Должен проверить поле email")
    void should_has_email() {
        UserUpdateDto dtoWithEmail = new UserUpdateDto();
        dtoWithEmail.setEmail("user@example.com");
        dtoWithEmail.isNotBlank(dtoWithEmail.getEmail());

        assertTrue(dtoWithEmail.hasEmail(), "hasEmail должен вернуть true, если email установлен");

        UserUpdateDto dtoWithoutEmail = new UserUpdateDto();
        assertFalse(dtoWithoutEmail.hasEmail(), "hasEmail должен вернуть false, если email не установлен");
    }
}