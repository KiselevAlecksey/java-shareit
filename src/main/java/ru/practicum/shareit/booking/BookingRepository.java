package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

import java.util.Optional;

public interface BookingRepository {

    Booking create(Booking booking);

    Optional<Booking> getById(Long id);

    Booking update(Booking booking);

    Booking updateConfirm(Booking booking);

    void delete(Long id);
}
