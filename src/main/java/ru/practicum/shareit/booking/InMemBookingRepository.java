package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InMemBookingRepository implements BookingRepository {

    private long countId = 0;

    private final Map<Long, Booking> bookingMap;

    private final ItemRepository itemRepository;

    @Override
    public Optional<Booking> create(Booking booking) {
        countId++;

        booking.setId(countId);

        bookingMap.put(countId, booking);

        Item item = itemRepository.get(booking.getOwnerId(), booking.getItemId()).orElseThrow(
                () -> new NotFoundException("Предмет не найден")
        );

        itemRepository.saveBookingItem(booking.getOwnerId(), booking.getId(), item);

        return getBooking(countId);
    }

    @Override
    public Optional<Booking> getById(Long id) {
        return getBooking(id);
    }

    @Override
    public Optional<Booking> update(Booking booking) {

        bookingMap.put(booking.getId(), booking);

        updateItemRepository(booking);

        return getBooking(booking.getId());
    }



    @Override
    public Optional<Booking> updateConfirm(Booking booking) {

        updateItemRepository(booking);

        bookingMap.put(booking.getId(), booking);

        return getBooking(booking.getId());
    }

    @Override
    public void delete(Long id) {

        Booking booking = bookingMap.get(id);

        bookingMap.remove(id);

        Item item = itemRepository.get(booking.getOwnerId(), booking.getItemId()).orElseThrow(
                () -> new NotFoundException("Предмет не найден")
        );

        itemRepository.deleteBookingItem(booking.getOwnerId(), booking.getId(), item);
    }

    private Optional<Booking> getBooking(Long bookingId) {
        Booking booking = bookingMap.get(bookingId);

        if (booking == null) {
            throw new NotFoundException("Бронь не найдена");
        }

        return Optional.of(booking);
    }

    private void updateItemRepository(Booking booking) {
        Item item = itemRepository.get(booking.getOwnerId(), booking.getItemId()).orElseThrow(
                () -> new NotFoundException("Предмет не найден")
        );

        itemRepository.updateBookingItem(booking.getOwnerId(), booking.getId(), item);
    }
}
