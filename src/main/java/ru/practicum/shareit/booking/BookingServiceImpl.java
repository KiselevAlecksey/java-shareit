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
    private final BookingRepository bookingRepository;

    private final ItemRepository itemRepository;

    @Override
    public BookingResponse create(BookingDto bookingRequest) {

        //validator.validateBookingRequest(bookingRequest);

        Booking booking = BookingMapper.mapToBooking(bookingRequest);

        Booking bookingCreated = bookingRepository.create(booking).orElseThrow(
                () -> new NotFoundException("Бронь не удалось сохранить")
        );

        Item item = itemRepository.get(booking.getOwnerId(), booking.getItemId()).orElseThrow(
                () -> new NotFoundException("Предмет не найден")
        );

        itemRepository.saveBookingItem(booking.getOwnerId(), booking.getId(), item);

        return BookingMapper.mapToBookingResponse(bookingCreated);
    }

    @Override
    public BookingResponse getById(Long id) {

        Booking booking = bookingRepository.getById(id).orElseThrow(
                () -> new NotFoundException("Бронь не удалось сохранить")
        );

        return BookingMapper.mapToBookingResponse(booking);
    }

    @Override
    public BookingResponse update(BookingDto bookingRequest, long id) {

        //validator.validateBookingRequest(bookingRequest);

        Booking booking = bookingRepository.getById(id).orElseThrow(
                () -> new NotFoundException("Бронь не найдена")
        );

        BookingMapper.updateBookingFields(booking, bookingRequest);

        Booking bookingUpdated = bookingRepository.update(booking).orElseThrow(
                () -> new NotFoundException("Бронь не удалось обновить")
        );

        updateItemRepository(booking);

        return BookingMapper.mapToBookingResponse(bookingUpdated);
    }

    @Override
    public BookingResponse updateConfirm(UpdateBookingConfirmResponse bookingRequest, long id) {
        //validator.validateReviewRequest(reviewRequest);

        Booking booking = bookingRepository.getById(id).orElseThrow(
                () -> new NotFoundException("Бронь не найдена")
        );

        BookingMapper.updateBookingConfirm(booking, bookingRequest);

        Booking bookingUpdated = bookingRepository.updateConfirm(booking).orElseThrow(
                () -> new NotFoundException("Бронь не удалось обновить")
        );

        updateItemRepository(booking);

        return BookingMapper.mapToBookingResponse(bookingUpdated);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new NotFoundException("Бронь не найдена");
        }

        bookingRepository.getById(id).orElseThrow(
                () -> new NotFoundException("Бронь не найдена")
        );

        Booking booking = bookingRepository.getById(id).orElseThrow(
                () -> new NotFoundException("Бронь не удалось сохранить")
        );

        Item item = itemRepository.get(booking.getOwnerId(), booking.getItemId()).orElseThrow(
                () -> new NotFoundException("Предмет не найден")
        );

        itemRepository.deleteBookingItem(booking.getOwnerId(), booking.getId(), item);

        bookingRepository.delete(id);
    }

    private void updateItemRepository(Booking booking) {
        Item item = itemRepository.get(booking.getOwnerId(), booking.getItemId()).orElseThrow(
                () -> new NotFoundException("Предмет не найден")
        );

        itemRepository.updateBookingItem(booking.getOwnerId(), booking.getId(), item);
    }
}
