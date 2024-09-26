package ru.practicum.shareit.booking;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemBookingRepository implements BookingRepository {

    private long countId = 0;

    private final Map<Long, Booking> bookingMap = new HashMap<>();

    @Override
    public Booking create(Booking booking) {
        countId++;

        booking.setId(countId);

        bookingMap.put(countId, booking);

        return getById(countId).orElseThrow(() -> new NotFoundException("Не удалось создать бронь"));
    }

    @Override
    public Optional<Booking> getById(Long id) {
        Booking booking = bookingMap.get(id);

        if (booking == null) {
            throw new NotFoundException("Бронь не найдена");
        }

        return Optional.of(booking);
    }

    @Override
    public Booking update(Booking booking) {

        bookingMap.put(booking.getId(), booking);

        return getById(booking.getId()).orElseThrow(() -> new NotFoundException("Не удалось обновить бронь"));
    }



    @Override
    public Booking updateConfirm(Booking booking) {

        bookingMap.put(booking.getId(), booking);

        return getById(booking.getId()).orElseThrow(() -> new NotFoundException("Не удалось обновить бронь"));
    }

    @Override
    public void delete(Long id) {

        getById(id).orElseThrow(() -> new NotFoundException("Не удалось получить бронь"));

        bookingMap.remove(id);
    }
}
