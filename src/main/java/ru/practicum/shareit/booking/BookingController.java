package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingConfirmResponse;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse create(@RequestBody NewBookingRequest bookingRequest) {
        log.error("Create booking {} start", bookingRequest);
        BookingResponse created = bookingService.create(bookingRequest);
        log.error("Created booking {}", bookingRequest);
        return created;
    }

    @GetMapping("/{id}")
    public BookingResponse getById(@PathVariable Long id) {
        log.error("Get booking by id {} start", id);
        BookingResponse booking = bookingService.getById(id);
        log.error("Get booking by id {} complete", id);
        return booking;
    }

    @PatchMapping
    public BookingResponse update(@RequestBody UpdateBookingRequest bookingRequest) {
        log.error("update booking {} start", bookingRequest);
        BookingResponse updated = bookingService.update(bookingRequest);
        log.error("updated booking {}", bookingRequest);
        return updated;
    }

    @PatchMapping("/confirm")
    public BookingResponse updateConfirm(@RequestBody UpdateBookingConfirmResponse bookingRequest) {
        log.error("update booking {} start", bookingRequest);
        BookingResponse updated = bookingService.updateConfirm(bookingRequest);
        log.error("updated booking {}", bookingRequest);
        return updated;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.error("Delete booking id {} start", id);
        bookingService.delete(id);
        log.error("Deleted booking id is {} complete", id);
    }
}
