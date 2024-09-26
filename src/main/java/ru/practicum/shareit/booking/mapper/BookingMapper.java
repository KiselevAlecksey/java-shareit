package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.UpdateBookingConfirmResponse;
import ru.practicum.shareit.booking.model.Booking;

import java.time.Duration;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public Booking mapToBooking(BookingDto booking) {

        return Booking.builder()
                .ownerId(booking.getOwnerId())
                .itemId(booking.getItemId())
                .available(true)
                .startBooking(booking.getStartBooking())
                .duration(Duration.ofMillis(booking.getDuration()))
                .consumerId(booking.getConsumerId())
                .build();
    }

    public BookingResponse mapToBookingResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getOwnerId(),
                booking.getItemId(),
                booking.getAvailable(),
                booking.getStartBooking(),
                booking.getDuration().toMillis(),
                booking.getConsumerId(),
                booking.getIsConfirm(),
                booking.getConfirmTime()
        );
    }

    public Booking updateBookingFields(Booking booking, BookingDto request) {
        if (request.hasDuration()) {
            booking.setDuration(Duration.ofMillis(request.getDuration()));
        }
        if (request.hasContent()) {
            booking.setContent(request.getContent());
        }

        return booking;
    }

    public Booking updateBookingConfirm(Booking booking, UpdateBookingConfirmResponse request) {
        if (request.hasIsConfirm()) {
            booking.setIsConfirm(request.getIsConfirm());
        }

        return booking;
    }
}
