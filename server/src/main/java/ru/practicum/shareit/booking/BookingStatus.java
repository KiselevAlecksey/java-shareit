package ru.practicum.shareit.booking;

import java.util.Optional;

public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    ALL,
    CURRENT,
    PAST,
    FUTURE;

    static Optional<BookingStatus> from(String status) {
        for (BookingStatus value : values()) {
            if (value.name().equals(status)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
