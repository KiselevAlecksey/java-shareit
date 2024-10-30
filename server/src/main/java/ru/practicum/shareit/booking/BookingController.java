package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.*;

import java.util.List;

import static ru.practicum.shareit.util.Const.USER_ID_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto create(@RequestBody BookingCreateDto bookingRequest) {
        BookingResponseDto created = bookingService.create(bookingRequest);
        return created;
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(
            @RequestHeader(USER_ID_HEADER) long userId,
            @PathVariable long bookingId) {
        BookingResponseDto booking = bookingService.getById(bookingId, userId);
        return booking;
    }

    @PatchMapping("/{bookerId}/{bookingId}")
    public BookingResponseDto update(@RequestBody BookingUpdateDto bookingRequest) {
        BookingResponseDto updated = bookingService.update(bookingRequest);
        return updated;
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approve(@RequestBody BookingApproveDto bookingApproveDto) {
        BookingResponseDto updated = bookingService.approve(bookingApproveDto);
        return updated;
    }

    @DeleteMapping("/{bookingId}")
    public void delete(@RequestHeader(USER_ID_HEADER) long bookerId,
                       @PathVariable long bookingId) {
        bookingService.delete(bookingId, bookerId);
    }

    @GetMapping
    public List<BookingResponseDto> getAllBookingsByBookerId(
            @RequestHeader(USER_ID_HEADER) long bookerId,
            @RequestParam(defaultValue = "all") BookingStatus state) {
        List<BookingResponseDto> bookings = bookingService.getAllBookingsByBookerId(bookerId, state);
        return bookings;
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllBookingsByOwnerId(
            @RequestHeader(USER_ID_HEADER) long ownerId,
            @RequestParam(defaultValue = "all") BookingStatus state) {
        List<BookingResponseDto> bookings = bookingService.getAllBookingsByOwnerId(ownerId, state);
        return bookings;
    }
}
