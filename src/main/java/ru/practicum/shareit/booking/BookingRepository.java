package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

import java.util.Optional;

public interface BookingRepository {

    Optional<Booking> create(Booking booking);

    Optional<Booking> getById(Long id);

    Optional<Booking> update(Booking booking);

    Optional<Booking> updateConfirm(Booking booking);

    void delete(Long id);
}
