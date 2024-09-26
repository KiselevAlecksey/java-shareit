package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.UpdateBookingConfirmResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingServiceImpl implements BookingService {
    final BookingRepository bookingRepository;

    final ItemRepository itemRepository;

    final BookingMapper bookingMapper;

    @Override
    public BookingResponse create(BookingDto bookingRequest) {

        Booking booking = bookingMapper.mapToBooking(bookingRequest);

        Booking bookingCreated = bookingRepository.create(booking);

        Item item = itemRepository.get(booking.getOwnerId(), booking.getItemId()).orElseThrow(
                () -> new NotFoundException("Предмет не найден")
        );

        itemRepository.saveBookingItem(item);

        return bookingMapper.mapToBookingResponse(bookingCreated);
    }

    @Override
    public BookingResponse getById(long id) {

        Booking booking = bookingRepository.getById(id).orElseThrow(
                () -> new NotFoundException("Бронь не удалось сохранить")
        );

        return bookingMapper.mapToBookingResponse(booking);
    }

    @Override
    public BookingResponse update(BookingDto bookingRequest, long id) {

        Booking booking = bookingRepository.getById(id).orElseThrow(
                () -> new NotFoundException("Бронь не найдена")
        );

        bookingMapper.updateBookingFields(booking, bookingRequest);

        Booking bookingUpdated = bookingRepository.update(booking);

        updateItemRepository(booking);

        return bookingMapper.mapToBookingResponse(bookingUpdated);
    }

    @Override
    public BookingResponse updateConfirm(UpdateBookingConfirmResponse bookingRequest, long id) {

        Booking booking = bookingRepository.getById(id).orElseThrow(
                () -> new NotFoundException("Бронь не найдена")
        );

        bookingMapper.updateBookingConfirm(booking, bookingRequest);

        Booking bookingUpdated = bookingRepository.updateConfirm(booking);

        updateItemRepository(booking);

        return bookingMapper.mapToBookingResponse(bookingUpdated);
    }

    @Override
    public void delete(long id) {

        bookingRepository.getById(id).orElseThrow(
                () -> new NotFoundException("Бронь не найдена")
        );

        Booking booking = bookingRepository.getById(id).orElseThrow(
                () -> new NotFoundException("Бронь не удалось сохранить")
        );

        Item item = itemRepository.get(booking.getOwnerId(), booking.getItemId()).orElseThrow(
                () -> new NotFoundException("Предмет не найден")
        );

        itemRepository.deleteBookingItem(item);

        bookingRepository.delete(id);
    }

    private void updateItemRepository(Booking booking) {
        Item item = itemRepository.get(booking.getOwnerId(), booking.getItemId()).orElseThrow(
                () -> new NotFoundException("Предмет не найден")
        );

        itemRepository.updateBookingItem(item);
    }
}
