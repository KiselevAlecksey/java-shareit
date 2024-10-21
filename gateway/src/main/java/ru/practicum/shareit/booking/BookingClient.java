package ru.practicum.shareit.booking;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.client.BaseClient;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getAllBookingsByBookerId(long userId, BookingStatus state) {
        Map<String, Object> parameters = Map.of(
                "state", state.name()
        );
        return get("?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> getAllBookingsByOwnerId(long userId, BookingStatus state) {
        Map<String, Object> parameters = Map.of(
                "state", state.name()
        );
        return get("/owner?state={state}", userId, parameters);
    }


    public ResponseEntity<Object> create(BookingCreateDto requestDto) {
        return post("", requestDto);
    }

    public ResponseEntity<Object> update(BookingUpdateDto requestDto) {
        return patch("/" + requestDto.getBookerId() + "/" + requestDto.getId(), requestDto);
    }

    public ResponseEntity<Object> approve(BookingApproveDto requestDto) {
        return patch("/" + requestDto.getId(), requestDto);
    }

    public void delete(long bookingId, long bookerId) {
        delete("/" + bookerId + "/" + bookingId);
    }

    public ResponseEntity<Object> getById(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }
}
