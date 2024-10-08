package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.Instant;

/**
 * TODO Sprint add-bookings.
 */

@Data
@Builder(toBuilder = true)
public class Booking {

    Long id;

    Long ownerId;

    Long itemId;

    Boolean available;

    @Builder.Default
    Instant startBooking = Instant.ofEpochMilli(0L);

    @Builder.Default
    Duration duration = Duration.ofDays(1);

    Long consumerId;

    Boolean isConfirm;

    @Builder.Default
    Instant confirmTime = Instant.now().plusMillis(86_400_000L);

    String content;

}
