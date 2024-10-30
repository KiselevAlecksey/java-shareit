package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import static ru.practicum.shareit.util.Const.USER_ID_HEADER;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    final ItemRequestClient itemRequestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(
            @RequestHeader(USER_ID_HEADER) long userId,
            @RequestBody @Validated ItemRequestCreateDto createDto) {
        log.info("==> Create ItemRequest {}", createDto);
        createDto.setRequestorId(userId);
        ResponseEntity<Object> responseDto = itemRequestClient.create(createDto);
        log.info("<== Created ItemRequest {}", responseDto);
        return responseDto;
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUserId(@RequestHeader(USER_ID_HEADER) long requestorId) {
        log.info("==> Get all ItemRequest by userId {}", requestorId);
        ResponseEntity<Object> responseDto = itemRequestClient.getAllByUserId(requestorId);
        log.info("<== Get all ItemRequest by userId {}", requestorId);
        return responseDto;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        log.info("==> Get all ItemRequests ");
        ResponseEntity<Object> responseDto = itemRequestClient.getAll();
        log.info("<== Get all ItemRequests ");
        return responseDto;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@PathVariable long requestId) {
        log.info("==> Get ItemRequest by id {}", requestId);
        ResponseEntity<Object> responseDto = itemRequestClient.get(requestId);
        log.info("<== Get ItemRequest by id {}", responseDto);
        return responseDto;
    }
}
