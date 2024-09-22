package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.dto.UpdateItemRequestDto;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @GetMapping
    public Collection<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getAll(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto add(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody @Valid NewItemRequestDto item) {
        ItemRequestDto dto = itemRequestService.add(userId, item);
        log.info("Item create {} complete", item);
        return dto;
    }

    @PatchMapping("/{itemId}")
    public ItemRequestDto update(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId,
            @RequestBody @Valid UpdateItemRequestDto item) {

        ItemRequestDto itemRequestDto = itemRequestService.update(userId, itemId, item);
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
    public ItemRequestDto get(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId) {
        ItemRequestDto itemRequestDto = itemRequestService.get(userId, itemId);
        log.info("ItemRequest get userId {}, itemId {} complete", userId, itemId);
        return itemRequestDto;

    }

    @GetMapping("/search")
    public Collection<ItemRequestDto> search(@RequestParam(defaultValue = "") String text) {
        Collection<ItemRequestDto> itemRequestDtos = itemRequestService.search(text);
        log.info("ItemRequest search text {} complete", text);
        return itemRequestDtos;
    }
}
