package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingConfirmResponse;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.model.Booking;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static Booking mapToBooking(NewBookingRequest booking) {

        return Booking.builder()
                .ownerId(booking.getOwnerId())
                .itemId(booking.getItemId())
                .available(true)
                .startBooking(booking.getStartBooking())
                .duration(Duration.ofMillis(booking.getDuration()))
                .consumerId(booking.getConsumerId())
                .confirmTime(booking.getConfirmTime())
                .build();
    }

    public static Booking mapToBooking(BookingResponse booking) {

        return Booking.builder()
                .id(booking.getId())
                .ownerId(booking.getOwnerId())
                .itemId(booking.getItemId())
                .available(booking.getAvailable())
                .startBooking(booking.getStartBooking())
                .duration(Duration.ofMillis(booking.getDuration()))
                .consumerId(booking.getConsumerId())
                .confirmTime(booking.getConfirmTime())
                .build();
    }

    public static BookingResponse mapToBookingResponse(Booking booking) {
        BookingResponse dto = new BookingResponse();
        dto.setId(booking.getId());
        dto.setOwnerId(booking.getOwnerId());
        dto.setItemId(booking.getItemId());
        dto.setAvailable(booking.getAvailable());
        dto.setStartBooking(booking.getStartBooking());
        dto.setDuration(booking.getDuration().toMillis());
        dto.setConsumerId(booking.getConsumerId());
        dto.setConfirmTime(booking.getConfirmTime());

        return dto;
    }

    public static Booking updateBookingFields(Booking booking, UpdateBookingRequest request) {
        if (request.hasStartBooking()) {
            booking.setStartBooking(request.getStartBooking());
        }
        if (request.hasDuration()) {
            booking.setDuration(Duration.ofMillis(request.getDuration()));
        }
        if (request.hasIsConfirm()) {
            booking.setIsConfirm(request.getIsConfirm());
        }
        if (request.hasContent()) {
            booking.setContent(request.getContent());
        }

        return booking;
    }

    public static Booking updateBookingConfirm(Booking booking, UpdateBookingConfirmResponse request) {
        if (request.hasIsConfirm()) {
            booking.setIsConfirm(request.getIsConfirm());
        }

        return booking;
    }
}
