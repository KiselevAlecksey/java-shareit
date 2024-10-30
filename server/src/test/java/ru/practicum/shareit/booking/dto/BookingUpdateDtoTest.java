package ru.practicum.shareit.booking.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.practicum.shareit.util.TestDataFactory.NOW_DATE_TIME;
import static ru.practicum.shareit.util.TestDataFactory.bookingUpdateDto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@DisplayName("BookingUpdateDto")
class BookingUpdateDtoTest {

    @Test
    @DisplayName("Должен не быть в прошлом")
    void should_start_not_past() {
        BookingUpdateDto dto = bookingUpdateDto();
        dto.setStart(NOW_DATE_TIME.minusDays(1));

        assertFalse(dto.getStart().isAfter(NOW_DATE_TIME)
                || dto.getStart().isEqual(NOW_DATE_TIME), "Поле start должно быть в настоящем или будущем");
    }

    @Test
    @DisplayName("Должен быть в настоящем или будущем")
    void should_start_present_or_future() {
        BookingUpdateDto dto = bookingUpdateDto();
        dto.setStart(NOW_DATE_TIME);

        assertTrue(dto.getStart().isEqual(NOW_DATE_TIME), "Поле start должно быть в настоящем или будущем");
    }

    @Test
    @DisplayName("Должен не быть в прошлом")
    void should_end_not_past() {
        BookingUpdateDto dto = bookingUpdateDto();
        dto.setEnd(NOW_DATE_TIME.minusDays(1));

        assertFalse(dto.getEnd().isEqual(NOW_DATE_TIME), "Поле end должно быть в будущем");
    }

    @Test
    @DisplayName("Должен не быть в настоящем")
    void should_end_not_present() {
        BookingUpdateDto dto = bookingUpdateDto();
        dto.setEnd(NOW_DATE_TIME);

        assertFalse(dto.getEnd().isAfter(NOW_DATE_TIME), "Поле end должно быть в будущем");
    }

    @Test
    @DisplayName("Должен проверить поле start")
    void should_has_start() {
        BookingUpdateDto dtoWithStart = new BookingUpdateDto();
        dtoWithStart.setStart(LocalDateTime.now());
        assertTrue(dtoWithStart.hasStart(), "hasStart должен вернуть true, если start установлен");

        BookingUpdateDto dtoWithoutStart = new BookingUpdateDto();
        assertFalse(dtoWithoutStart.hasStart(), "hasStart должен вернуть false, если start не установлен");
    }

    @Test
    @DisplayName("Должен проверить поле end")
    void should_has_end() {
        BookingUpdateDto dtoWithEnd = new BookingUpdateDto();
        dtoWithEnd.setEnd(LocalDateTime.now());
        assertTrue(dtoWithEnd.hasEnd(), "hasEnd должен вернуть true, если end установлен");

        BookingUpdateDto dtoWithoutEnd = new BookingUpdateDto();
        assertFalse(dtoWithoutEnd.hasEnd(), "hasEnd должен вернуть false, если end не установлен");
    }

    @Test
    @DisplayName("Должен проверить поле content")
    void should_has_content() {
        BookingUpdateDto dtoWithContent = new BookingUpdateDto();
        dtoWithContent.setContent("Некоторое содержимое");
        assertTrue(dtoWithContent.hasContent(), "hasContent должен вернуть true, если содержимое установлено");

        BookingUpdateDto dtoWithBlankContent = new BookingUpdateDto();
        dtoWithBlankContent.setContent("");
        assertFalse(dtoWithBlankContent.hasContent(), "hasContent должен вернуть false для пустого содержимого");

        BookingUpdateDto dtoWithNullContent = new BookingUpdateDto();
        assertFalse(dtoWithNullContent.hasContent(), "hasContent должен вернуть false, если содержимое равно null");
    }
}