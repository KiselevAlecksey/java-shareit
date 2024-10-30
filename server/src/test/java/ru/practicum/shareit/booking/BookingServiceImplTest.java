package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.util.TestDataFactory.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@DisplayName("BookingService")
class BookingServiceImplTest {

    private final BookingService bookingService;

    @Test
    @DisplayName("Должен создавать новую бронь")
    void should_create_booking() {
        BookingResponseDto dto = bookingService.create(bookingCreateDto());

        long bookingId = dto.id();

        assertEquals(bookingCreatedDto(bookingId), dto, "Брони должны совпадать");
    }

    @Test
    @DisplayName("Должен получать бронь по ID")
    void should_get_booking_by_id() {
        BookingResponseDto dto = bookingService.getById(TEST_BOOKING_ID, TEST_USER_ID);

        assertEquals(bookingCreatedDto(), dto, "Брони должны совпадать");
    }

    @Test
    @DisplayName("Должен обновлять бронь")
    void should_update_booking() {
        BookingResponseDto dto = bookingService.update(bookingUpdateDto());

        assertEquals(bookingUpdatedDto(), dto, "Брони должны совпадать");
    }

    @Test
    @DisplayName("Должен одобрять бронь")
    void should_approve_booking() {
        BookingResponseDto dto = bookingService.approve(bookingApproveDto());

        assertEquals(bookingOneUpdatedDto(), dto, "Брони должны совпадать");
    }

    @Test
    @DisplayName("Должен удалять бронь")
    void should_delete_booking() {
        bookingService.delete(TEST_BOOKING_ID, TEST_USER2_ID);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            bookingService.getById(TEST_BOOKING_ID, TEST_USER2_ID);
        });

        assertEquals("Бронь не найдена", exception.getMessage(), "Сообщение исключения должно совпадать");
    }

    @Test
    @DisplayName("Должен получать все брони по ID забронировавшего")
    void should_get_all_bookings_by_booker_id() {

        List<BookingResponseDto> dtoList = bookingService
                .getAllBookingsByBookerId(TEST_USER_ID, BookingStatus.REJECTED);

        assertEquals(bookingCreatedDtoList(), dtoList, "Списки долждны совпадать");
    }

    @Test
    @DisplayName("Должен получать все брони по ID владельца")
    void should_get_all_bookings_by_owner_id() {

        List<BookingResponseDto> dtoList = bookingService
                .getAllBookingsByOwnerId(TEST_USER_ID, BookingStatus.ALL);

        assertEquals(bookingCreatedDtoList2(), dtoList, "Списки долждны совпадать");
    }
}