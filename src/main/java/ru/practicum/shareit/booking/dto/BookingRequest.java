package ru.practicum.shareit.booking.dto;

import java.time.Instant;

public interface BookingRequest {

    Long getOwnerId();

    default Long getItemId() {
        return null;
    }

    default Instant getStartBooking() {
        return null;
    }

    default Long getConsumerId() {
        return null;
    }

    default Boolean getIsConfirm() {
        return null;
    }
}
