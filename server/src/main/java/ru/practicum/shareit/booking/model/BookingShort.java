package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

public interface BookingShort {
    Item getItem();

    LocalDateTime getEndBooking();

    LocalDateTime getStartBooking();
}