package ru.practicum.shareit.booking.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class BookingResponse {

    Long id;

    @NotBlank
    Long ownerId;

    @NotBlank
    Long itemId;

    @NotNull
    Boolean available;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Instant startBooking;

    Long duration;

    @NotBlank
    Long consumerId;

    @NotNull
    @JsonProperty("isComplete")
    Boolean isConfirm;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Instant confirmTime = Instant.now().plusMillis(86_400_000L);
}
