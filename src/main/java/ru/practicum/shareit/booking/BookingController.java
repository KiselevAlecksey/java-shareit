package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.UpdateBookingConfirmResponse;
import ru.practicum.shareit.util.Marker;

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
    @Validated({Marker.OnCreate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse create(@RequestBody @Valid BookingDto bookingRequest) {
        BookingResponse created = bookingService.create(bookingRequest);
        log.info("Created booking {}", bookingRequest);
        return created;
    }

    @GetMapping("/{id}")
    public BookingResponse getById(@PathVariable Long id) {
        BookingResponse booking = bookingService.getById(id);
        log.info("Get booking by id {} complete", id);
        return booking;
    }

    @PatchMapping("/{id}")
    @Validated({Marker.OnUpdate.class})
    public BookingResponse update(@RequestBody @Valid BookingDto bookingRequest, @PathVariable long id) {
        BookingResponse updated = bookingService.update(bookingRequest, id);
        log.info("updated booking {}", bookingRequest);
        return updated;
    }

    @PatchMapping("/confirm/{id}")
    public BookingResponse updateConfirm(
            @RequestBody @Valid UpdateBookingConfirmResponse bookingRequest,
            @PathVariable long id) {
        BookingResponse updated = bookingService.updateConfirm(bookingRequest, id);
        log.info("updated booking {}", bookingRequest);
        return updated;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookingService.delete(id);
        log.info("Deleted booking id is {} complete", id);
    }
}
