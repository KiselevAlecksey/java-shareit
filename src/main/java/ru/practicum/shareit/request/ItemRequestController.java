package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @GetMapping
    public Collection<ItemRequestDtoResponse> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("ItemRequests get all start");
        return itemRequestService.getAll(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDtoResponse add(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody @Valid ItemRequestDto item) {
        ItemRequestDtoResponse dto = itemRequestService.add(userId, item);
        log.info("Item create {} complete", item);
        return dto;
    }

    @PatchMapping("/{itemId}")
    public ItemRequestDtoResponse update(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId,
            @RequestBody @Valid ItemRequestDto item) {

        ItemRequestDtoResponse itemRequestDto = itemRequestService.update(userId, itemId, item);
        log.info("ItemRequest updated {} complete", item);
        return itemRequestDto;

    }

    @DeleteMapping("/{itemId}")
    public void delete(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId) {
        itemRequestService.delete(userId, itemId);
        log.info("ItemRequest deleted userId {}, itemId {} complete", userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemRequestDtoResponse get(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId) {
        ItemRequestDtoResponse itemRequestDto = itemRequestService.get(userId, itemId);
        log.info("ItemRequest get userId {}, itemId {} complete", userId, itemId);
        return itemRequestDto;

    }

    @GetMapping("/search")
    public Collection<ItemRequestDtoResponse> search(@RequestParam(defaultValue = "") String text) {
        Collection<ItemRequestDtoResponse> itemRequestDtos = itemRequestService.search(text);
        log.info("ItemRequest search text {} complete", text);
        return itemRequestDtos;
    }
}
