package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.util.Marker;

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

    @NotNull(groups = Marker.OnCreate.class)
    Long ownerId;

    @NotNull(groups = Marker.OnCreate.class)
    Long itemId;

    @NotNull(groups = Marker.OnCreate.class)
    Instant startBooking;

    @Positive
    @Max(30)
    @Min(1)
    Long duration;

    @NotNull(groups = Marker.OnCreate.class)
    Long consumerId;

    String content;

    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasContent() {
        return content != null && !content.isBlank();
    }
}
