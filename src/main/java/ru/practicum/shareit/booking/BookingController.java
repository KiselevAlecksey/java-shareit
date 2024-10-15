package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.*;

import java.util.List;

import static ru.practicum.shareit.util.Const.USER_ID_HEADER;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto create(
            @RequestHeader(USER_ID_HEADER) long bookerId,
            @RequestBody @Validated BookingCreateDto bookingRequest) {
        log.info("==> Created booking {} start", bookingRequest);
        bookingRequest.setBookerId(bookerId);
        BookingResponseDto created = bookingService.create(bookingRequest);
        log.info("<== Created booking {} complete", bookingRequest);
        return created;
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(
            @RequestHeader(USER_ID_HEADER) long bookerId,
            @PathVariable long bookingId) {
        log.info("==> Get booking by id {} start", bookingId);
        BookingResponseDto booking = bookingService.getById(bookingId);
        log.info("<== Get booking by id {} complete", bookingId);
        return booking;
    }

    @PatchMapping("/{bookerId}/{bookingId}")
    public BookingResponseDto update(
            @RequestHeader(USER_ID_HEADER) long bookerId,
            @RequestBody @Validated BookingUpdateDto bookingRequest,
            @PathVariable long bookingId) {
        log.info("==> updated booking {} start", bookingRequest);
        bookingRequest.setId(bookingId);
        bookingRequest.setBookerId(bookerId);
        BookingResponseDto updated = bookingService.update(bookingRequest);
        log.info("<== updated booking {} complete", bookingRequest);
        return updated;
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approve(
            @RequestHeader(USER_ID_HEADER) long ownerId,
            @PathVariable long bookingId,
            @RequestParam(required = false) boolean approved) {
        log.info("==> Approved booking {} start", approved);
        BookingApproveDto bookingApproveDto = new BookingApproveDto();
        bookingApproveDto.setApproved(approved);
        bookingApproveDto.setId(bookingId);
        bookingApproveDto.setOwnerId(ownerId);
        BookingResponseDto updated = bookingService.approve(bookingApproveDto);
        log.info("<== Approved booking {} complete", approved);
        return updated;
    }

    @DeleteMapping("/{bookingId}")
    public void delete(@PathVariable long bookingId) {
        log.info("==> Deleted booking id is {} start", bookingId);
        bookingService.delete(bookingId);
        log.info("<== Deleted booking id is {} complete", bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getAllBookingsByBookerId(
            @RequestHeader(USER_ID_HEADER) long bookerId,
            @RequestParam(defaultValue = "all") String state) {
        log.info("==> Get all bookings userId {}, state {} start", bookerId, state);

        String status = state.toUpperCase();

        BookingStatus bookingStatus = BookingStatus.from(status)
                .orElseThrow(() -> new IllegalArgumentException("Не поддерживаемое состояние: " + status));

        List<BookingResponseDto> bookings = bookingService.getAllBookingsByBookerId(bookerId, bookingStatus);
        log.info("==> Get all bookings userId {}, state {} complete", bookerId, status);
        return bookings;
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllBookingsByOwnerId(
            @RequestHeader(USER_ID_HEADER) long ownerId,
            @RequestParam(defaultValue = "all") String state) {
        log.info("==> Get all bookings ownerId {}, state {} start", ownerId, state);

        String status = state.toUpperCase();

        BookingStatus bookingStatus = BookingStatus.from(status)
                .orElseThrow(() -> new IllegalArgumentException("Не поддерживаемое состояние: " + status));

        List<BookingResponseDto> bookings = bookingService.getAllBookingsByOwnerId(ownerId, bookingStatus);
        log.info("==> Get all bookings ownerId {}, state {} complete", ownerId, status);
        return bookings;
    }
}
