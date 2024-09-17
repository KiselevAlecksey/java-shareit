package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class NewBookingRequest implements BookingRequest {

    @NotBlank
    Long ownerId;

    @NotBlank
    Long itemId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull
    Instant startBooking;

    Long duration;

    @NotBlank
    Long consumerId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Instant confirmTime = Instant.now().plusMillis(86_400_000L);
}
