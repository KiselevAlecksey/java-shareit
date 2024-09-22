package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public Collection<ItemDtoResponse> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAll(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDtoResponse add(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody @Valid NewItemRequest item) {
        ItemDtoResponse dto = itemService.add(userId, item);
        log.info("Item created {} complete", item);
        return dto;
    }

    @PatchMapping("/{itemId}")
    public ItemDtoResponse update(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId,
            @RequestBody @Valid UpdateItemRequest item) {

        ItemDtoResponse dtoResponse = itemService.update(userId, itemId, item);
        log.info("Item updated {} complete", item);
        return dtoResponse;
    }

    @DeleteMapping("/{itemId}")
    public void delete(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId) {
        itemService.delete(userId, itemId);
        log.info("Item deleted userId {}, itemId {} complete", userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoResponse get(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId) {
        ItemDtoResponse dtoResponse = itemService.get(userId, itemId);
        log.info("Item get userId {}, itemId {} complete", userId, itemId);
        return dtoResponse;
    }

    @GetMapping("/search")
    public Collection<ItemDtoResponse> search(@RequestParam(defaultValue = "") String text) {
        Collection<ItemDtoResponse> itemDtoResponses = itemService.search(text);
        log.info("Item search text {} complete", text);
        return itemDtoResponses;
    }
}