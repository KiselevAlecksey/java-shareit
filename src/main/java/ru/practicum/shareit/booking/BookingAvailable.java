package ru.practicum.shareit.booking;

import java.time.LocalDateTime;

public interface BookingAvailable {
    String getAvailable();
    LocalDateTime getEndBooking();
}
