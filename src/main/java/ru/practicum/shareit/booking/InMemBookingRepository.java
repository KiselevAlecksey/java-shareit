package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InMemBookingRepository implements BookingRepository {

    private long countId = 0;

    private final Map<Long, Booking> bookingMap;

    @Override
    public Optional<Booking> create(Booking booking) {
        countId++;

        booking.setId(countId);

        bookingMap.put(countId, booking);

        return getBooking(countId);
    }

    @Override
    public Optional<Booking> getById(Long id) {
        return getBooking(id);
    }

    @Override
    public Optional<Booking> update(Booking booking) {

        bookingMap.put(booking.getId(), booking);

        return getBooking(booking.getId());
    }



    @Override
    public Optional<Booking> updateConfirm(Booking booking) {

        bookingMap.put(booking.getId(), booking);

        return getBooking(booking.getId());
    }

    @Override
    public void delete(Long id) {

        Booking booking = bookingMap.get(id);

        bookingMap.remove(id);

    }

    private Optional<Booking> getBooking(Long bookingId) {
        Booking booking = bookingMap.get(bookingId);

        if (booking == null) {
            throw new NotFoundException("Бронь не найдена");
        }

        return Optional.of(booking);
    }


}
