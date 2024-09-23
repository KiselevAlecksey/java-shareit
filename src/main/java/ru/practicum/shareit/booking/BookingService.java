package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.UpdateBookingConfirmResponse;

public interface BookingService {

    BookingResponse create(BookingDto bookingRequest);

    BookingResponse getById(Long id);

    BookingResponse update(BookingDto bookingRequest, long id);

    BookingResponse updateConfirm(UpdateBookingConfirmResponse bookingRequest, long id);

    void delete(Long id);
}
