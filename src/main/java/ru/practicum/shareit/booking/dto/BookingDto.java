package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {

    Long ownerId;

    Long itemId;

    Instant startBooking;

    @Positive
    @Max(30)
    @Min(1)
    Long duration;

    Long consumerId;

    String content;

    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasContent() {
        return content != null && !content.isBlank();
    }
}
