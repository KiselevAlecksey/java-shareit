package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class BookingMapper {
    final UserMapper userMapper;
    final ItemMapper itemMapper;
    final CommentMapper commentMapper;

    public Booking mapToBooking(BookingCreateDto booking) {

        return Booking.builder()
                .available(true)
                .startBooking(booking.getStart())
                .endBooking(booking.getEnd())
                .build();
    }

    public BookingResponseDto mapToBookingResponse(Booking booking) {
        String start = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(booking.getStartBooking());

        String end = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(booking.getEndBooking());

        String availableTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(booking.getConfirmTime());

        return new BookingResponseDto(
                booking.getId(),
                itemMapper.mapToItemDto(booking.getItem()),
                start,
                end,
                userMapper.mapToUserDto(booking.getBooker()),
                booking.getStatus().name(),
                availableTime
        );
    }

    public Booking updateBookingFields(Booking booking, BookingUpdateDto request) {
        if (request.hasStart()) {
            booking.setStartBooking(request.getStart());
        }

        if (request.hasEnd()) {
            booking.setEndBooking(request.getEnd());
        }

        if (request.hasContent()) {
            booking.setContent(request.getContent());
        }
        return booking;
    }

    private String instantToString(Instant instant) {

        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return zonedDateTime.format(formatter);
    }
}
