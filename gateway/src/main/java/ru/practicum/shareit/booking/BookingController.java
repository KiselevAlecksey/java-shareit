package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.*;

import static ru.practicum.shareit.util.Const.USER_ID_HEADER;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(
            @RequestHeader(USER_ID_HEADER) long bookerId,
            @RequestBody @Validated BookingCreateDto bookingRequest) {
        log.info("==> Created booking {} start", bookingRequest);
        bookingRequest.setBookerId(bookerId);
        ResponseEntity<Object> created = bookingClient.create(bookingRequest);
        log.info("<== Created booking {} complete", bookingRequest);
        return created;
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(
            @RequestHeader(USER_ID_HEADER) long userId,
            @PathVariable long bookingId) {
        log.info("==> Get booking by id {} start", bookingId);
        ResponseEntity<Object> booking = bookingClient.getById(userId, bookingId);
        log.info("<== Get booking by id {} complete", bookingId);
        return booking;
    }

    @PatchMapping("/{bookerId}/{bookingId}")
    public ResponseEntity<Object> update(
            @RequestHeader(USER_ID_HEADER) long bookerId,
            @RequestBody @Validated BookingUpdateDto bookingRequest,
            @PathVariable long bookingId) {
        log.info("==> updated booking {} start", bookingRequest);
        bookingRequest.setId(bookingId);
        bookingRequest.setBookerId(bookerId);
        ResponseEntity<Object> updated = bookingClient.update(bookingRequest);
        log.info("<== updated booking {} complete", bookingRequest);
        return updated;
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(
            @RequestHeader(USER_ID_HEADER) long ownerId,
            @PathVariable long bookingId,
            @RequestParam(required = false) boolean approved) {
        log.info("==> Approved booking {} start", approved);
        BookingApproveDto bookingApproveDto = new BookingApproveDto();
        bookingApproveDto.setApproved(approved);
        bookingApproveDto.setId(bookingId);
        bookingApproveDto.setOwnerId(ownerId);
        ResponseEntity<Object> updated = bookingClient.approve(bookingApproveDto);
        log.info("<== Approved booking {} complete", approved);
        return updated;
    }

    @DeleteMapping("/{bookingId}")
    public void delete(@RequestHeader(USER_ID_HEADER) long bookerId,
                       @PathVariable long bookingId) {
        log.info("==> Deleted booking id is {} start", bookingId);
        bookingClient.delete(bookingId, bookerId);
        log.info("<== Deleted booking id is {} complete", bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingsByBookerId(
            @RequestHeader(USER_ID_HEADER) long bookerId,
            @RequestParam(defaultValue = "all") String state) {
        log.info("==> Get all bookings userId {}, state {} start", bookerId, state);

        String status = state.toUpperCase();

        BookingStatus bookingStatus = BookingStatus.from(status)
                .orElseThrow(() -> new IllegalArgumentException("Не поддерживаемое состояние: " + status));

        ResponseEntity<Object> bookings = bookingClient.getAllBookingsByBookerId(bookerId, bookingStatus);
        log.info("==> Get all bookings userId {}, state {} complete", bookerId, status);
        return bookings;
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsByOwnerId(
            @RequestHeader(USER_ID_HEADER) long ownerId,
            @RequestParam(defaultValue = "all") String state) {
        log.info("==> Get all bookings ownerId {}, state {} start", ownerId, state);

        String status = state.toUpperCase();

        BookingStatus bookingStatus = BookingStatus.from(status)
                .orElseThrow(() -> new IllegalArgumentException("Не поддерживаемое состояние: " + status));

        ResponseEntity<Object> bookings = bookingClient.getAllBookingsByOwnerId(ownerId, bookingStatus);
        log.info("==> Get all bookings ownerId {}, state {} complete", ownerId, status);
        return bookings;
    }
}
