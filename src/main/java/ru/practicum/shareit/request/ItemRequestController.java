package ru.practicum.shareit.request;

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
            @RequestBody NewItemRequestDto item) {
        log.error("Item create {} start", item);
        ItemRequestDto dto = itemRequestService.add(userId, item);
        log.error("Item create {} complete", item);
        return dto;
    }

    @PatchMapping("/{itemId}")
    public ItemRequestDto update(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId,
            @RequestBody UpdateItemRequestDto item) {
        return itemRequestService.update(userId, itemId, item);
    }

    @DeleteMapping("/{itemId}")
    public void delete(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId) {
        itemRequestService.delete(userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemRequestDto get(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId) {
        return itemRequestService.get(userId, itemId);
    }

    @GetMapping("/search")
    public Collection<ItemRequestDto> search(@RequestParam(defaultValue = "") String text) {
        return itemRequestService.search(text);
    }
}
