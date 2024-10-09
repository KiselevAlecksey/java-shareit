package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.user.model.User;

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
            @RequestHeader(USER_ID_HEADER) long userId,
            @RequestBody @Valid BookingCreateDto bookingRequest) {
        log.info("==> Created booking {} start", bookingRequest);
        bookingRequest.setConsumerId(userId);
        BookingResponseDto created = bookingService.create(bookingRequest);
        log.info("<== Created booking {} complete", bookingRequest);
        return created;
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(
            @RequestHeader(USER_ID_HEADER) long userId,
            @PathVariable long bookingId) {
        log.info("==> Get booking by id {} start", bookingId);
        BookingResponseDto booking = bookingService.getById(userId, bookingId);
        log.info("<== Get booking by id {} complete", bookingId);
        return booking;
    }

    @PatchMapping("/{bookerId}/{bookingId}")
    public BookingResponseDto update(
            @RequestHeader(USER_ID_HEADER) long userId,
            @RequestBody @Valid BookingUpdateDto bookingRequest,
            @PathVariable long bookingId) {
        log.info("==> updated booking {} start", bookingRequest);
        bookingRequest.setId(bookingId);
        bookingRequest.setConsumer(new User());
        bookingRequest.getConsumer().setId(userId);
        BookingResponseDto updated = bookingService.update(bookingRequest);
        log.info("<== updated booking {} complete", bookingRequest);
        return updated;
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approve(
            @RequestHeader(USER_ID_HEADER) long userId,
            @PathVariable long bookingId,
            @RequestParam(required = false) boolean approved) {
        log.info("==> Approved booking {} start", approved);
        BookingApproveDto bookingApproveDto = new BookingApproveDto();
        bookingApproveDto.setApproved(approved);
        bookingApproveDto.setId(bookingId);
        bookingApproveDto.setOwner(User.builder().id(userId).build());
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
    public List<BookingResponseDto> getAllBookingsByConsumerId(
            @RequestHeader(USER_ID_HEADER) long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        log.info("==> Get all bookings userId {}, state {} start", userId, state);
        List<BookingResponseDto> bookings = bookingService.getAllBookingsByConsumerId(userId, state);
        log.info("==> Get all bookings userId {}, state {} complete", userId, state);
        return bookings;
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllBookingsByOwnerId(
            @RequestHeader(USER_ID_HEADER) long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        log.info("==> Get all bookings ownerId {}, state {} start", userId, state);
        List<BookingResponseDto> bookings = bookingService.getAllBookingsByOwnerId(userId, state);
        log.info("==> Get all bookings ownerId {}, state {} complete", userId, state);
        return bookings;
    }
}
