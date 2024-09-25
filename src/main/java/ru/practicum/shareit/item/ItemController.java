package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.util.Marker;

import java.util.List;

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
    public List<ItemDtoResponse> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAll(userId);
    }

    @PostMapping
    @Validated({Marker.OnCreate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDtoResponse add(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody @Valid ItemDto item) {

        log.info("Item create {} start", item);
        ItemDtoResponse dto = itemService.add(userId, item);
        log.info("Item created {} complete", item);
        return dto;
    }

    @PatchMapping("/{itemId}")
    @Validated({Marker.OnUpdate.class})
    public ItemDtoResponse update(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId,
            @RequestBody @Valid ItemDto item) {
        log.info("Item update {} start", item);
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
    public List<ItemDtoResponse> search(@RequestParam(defaultValue = "") String text) {
        List<ItemDtoResponse> itemDtoResponses = itemService.search(text);
        log.info("Item search text {} complete", text);
        return itemDtoResponses;
    }
}
