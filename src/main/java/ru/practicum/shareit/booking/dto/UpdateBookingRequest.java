package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;

@Data
public class UpdateBookingRequest implements BookingRequest {

    Long id;

    @NotBlank
    Long ownerId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Instant startBooking;

    Long duration;

    @NotBlank
    Long consumerId;

    @JsonProperty("isConfirm")
    Boolean isConfirm;

    String content;

    public boolean hasStartBooking() {
        return startBooking != null;
    }

    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasIsConfirm() {
        return isConfirm != null && !isConfirm;
    }

    public boolean hasContent() {
        return content != null && !content.isBlank();
    }
}

