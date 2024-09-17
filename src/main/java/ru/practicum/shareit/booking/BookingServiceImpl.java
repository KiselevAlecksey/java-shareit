package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingConfirmResponse;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    public BookingResponse create(NewBookingRequest bookingRequest) {

        //validator.validateBookingRequest(bookingRequest);

        Booking booking = BookingMapper.mapToBooking(bookingRequest);

        Booking bookingCreated = bookingRepository.create(booking).orElseThrow(
                () -> new NotFoundException("Бронь не удалось сохранить")
        );

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
    public BookingResponse update(UpdateBookingRequest bookingRequest) {

        //validator.validateBookingRequest(bookingRequest);

        Booking booking = bookingRepository.getById(bookingRequest.getId()).orElseThrow(
                () -> new NotFoundException("Бронь не найдена")
        );

        BookingMapper.updateBookingFields(booking, bookingRequest);

        Booking bookingUpdated = bookingRepository.update(booking).orElseThrow(
                () -> new NotFoundException("Бронь не удалось обновить")
        );

        return BookingMapper.mapToBookingResponse(bookingUpdated);
    }

    @Override
    public BookingResponse updateConfirm(UpdateBookingConfirmResponse bookingRequest) {
        //validator.validateReviewRequest(reviewRequest);

        Booking booking = bookingRepository.getById(bookingRequest.getId()).orElseThrow(
                () -> new NotFoundException("Бронь не найдена")
        );

        BookingMapper.updateBookingConfirm(booking, bookingRequest);

        Booking bookingUpdated = bookingRepository.updateConfirm(booking).orElseThrow(
                () -> new NotFoundException("Бронь не удалось обновить")
        );

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

        bookingRepository.delete(id);
    }
}
