package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto create(BookingCreateDto bookingRequest);

    BookingResponseDto getById(long bookingId,  long userId);

    BookingResponseDto update(BookingUpdateDto bookingRequest);

    BookingResponseDto approve(BookingApproveDto bookingRequest);

    void delete(long bookingId);

    List<BookingResponseDto> getAllBookingsByBookerId(long bookerId, BookingStatus status);

    List<BookingResponseDto> getAllBookingsByOwnerId(long ownerId, BookingStatus status);
}
