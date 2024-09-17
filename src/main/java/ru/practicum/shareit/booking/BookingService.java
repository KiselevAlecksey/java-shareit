package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingConfirmResponse;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;

public interface BookingService {

    BookingResponse create(NewBookingRequest bookingRequest);

    BookingResponse getById(Long id);

    BookingResponse update(UpdateBookingRequest bookingRequest);

    BookingResponse updateConfirm(UpdateBookingConfirmResponse bookingRequest);

    void delete(Long id);
}
