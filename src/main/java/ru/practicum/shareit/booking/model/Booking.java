package ru.practicum.shareit.booking.model;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    Long ownerId;

    @NotBlank
    Long itemId;

    Boolean available;

    @Builder.Default
    Instant startBooking = Instant.ofEpochMilli(0L);

    @Builder.Default
    Duration duration = Duration.ofDays(1);

    @NotBlank
    Long consumerId;

    Boolean isConfirm;

    @Builder.Default
    Instant confirmTime = Instant.now().plusMillis(86_400_000L);

    String content;

}
