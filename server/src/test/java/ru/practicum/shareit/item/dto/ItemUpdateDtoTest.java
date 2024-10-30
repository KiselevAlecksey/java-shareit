package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ItemUpdateDto")
class ItemUpdateDtoTest {

    @Test
    @DisplayName("Должен проверить поле name")
    void should_has_name() {
        ItemUpdateDto dtoWithName = new ItemUpdateDto();
        dtoWithName.setName("Название предмета");
        dtoWithName.isNotBlank(dtoWithName.getName());

        assertTrue(dtoWithName.hasName(), "hasName должен вернуть true, если имя установлено");

        ItemUpdateDto dtoWithoutName = new ItemUpdateDto();
        assertFalse(dtoWithoutName.hasName(), "hasName должен вернуть false, если имя не установлено");
    }

    @Test
    @DisplayName("Должен проверить поле description")
    void should_has_description() {
        ItemUpdateDto dtoWithDescription = new ItemUpdateDto();
        dtoWithDescription.setDescription("Описание предмета");
        dtoWithDescription.isNotBlank(dtoWithDescription.getDescription());
        assertTrue(dtoWithDescription.hasDescription(), "hasDescription должен вернуть true, если описание установлено");

        ItemUpdateDto dtoWithoutDescription = new ItemUpdateDto();
        assertFalse(dtoWithoutDescription.hasDescription(), "hasDescription должен вернуть false, если описание не установлено");
    }

    @Test
    @DisplayName("Должен проверить поле available")
    void should_has_available() {
        ItemUpdateDto dtoWithAvailable = new ItemUpdateDto();
        dtoWithAvailable.setAvailable(true);
        assertTrue(dtoWithAvailable.hasAvailable(), "hasAvailable должен вернуть true, если доступность установлена");

        ItemUpdateDto dtoWithoutAvailable = new ItemUpdateDto();
        assertFalse(dtoWithoutAvailable.hasAvailable(), "hasAvailable должен вернуть false, если доступность не установлена");
    }
}