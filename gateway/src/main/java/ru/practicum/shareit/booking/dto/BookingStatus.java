package ru.practicum.shareit.booking.dto;

import java.util.Optional;

public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    ALL,
    CURRENT,
    PAST,
    FUTURE;

    public static Optional<BookingStatus> from(String status) {
        for (BookingStatus value : BookingStatus.values()) {
            if (value.name().equals(status)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
