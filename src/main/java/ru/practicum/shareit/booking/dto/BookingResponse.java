package ru.practicum.shareit.booking.dto;

import java.time.Instant;

public record BookingResponse(
        Long id,

        Long ownerId,

        Long itemId,

        Boolean available,

        Instant startBooking,

        Long duration,

        Long consumerId,

        Boolean isConfirm,

        Instant confirmTime) {
}
