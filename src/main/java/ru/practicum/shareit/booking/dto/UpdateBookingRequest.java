package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;

@Data
public class UpdateBookingRequest {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Instant startBooking;

    Long duration;

    @NotBlank
    Long consumerId;

    String content;

    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasContent() {
        return content != null && !content.isBlank();
    }
}

