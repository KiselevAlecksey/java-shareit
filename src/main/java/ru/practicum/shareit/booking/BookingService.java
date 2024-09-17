package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.UpdateBookingConfirmResponse;

public interface BookingService {

    BookingResponse create(BookingDto bookingRequest);

    BookingResponse getById(long id);

    BookingResponse update(BookingDto bookingRequest, long id);

    BookingResponse updateConfirm(UpdateBookingConfirmResponse bookingRequest, long id);

    void delete(long id);
}
