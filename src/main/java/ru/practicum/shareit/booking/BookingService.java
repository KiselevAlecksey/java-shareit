package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto create(BookingCreateDto bookingRequest);

    BookingResponseDto getById(long userId, long bookingId);

    BookingResponseDto update(BookingUpdateDto bookingRequest);

    BookingResponseDto approve(BookingApproveDto bookingRequest);

    void delete(long id);

    List<BookingResponseDto> getAllBookingsByConsumerId(long id, String state);

    List<BookingResponseDto> getAllBookingsByOwnerId(long id, String state);
}
