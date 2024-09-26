package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping
    public List<ItemDtoResponse> getAll(@RequestHeader(USER_ID_HEADER) long userId) {
        log.info("==> Users get all start");
        List<ItemDtoResponse> items = itemService.getAll(userId);
        log.info("<== Users get all start");
        return items;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDtoResponse create(
            @RequestHeader(USER_ID_HEADER) long userId,
            @RequestBody @Valid ItemCreateDto item) {
        log.info("==> Item create {} start", item);
        item.setUserId(userId);
        ItemDtoResponse dto = itemService.create(item);
        log.info("<== Item created {} complete", item);
        return dto;
    }

    @PatchMapping("/{itemId}")
    public ItemDtoResponse update(
            @RequestHeader(USER_ID_HEADER) long userId,
            @PathVariable long itemId,
            @RequestBody @Valid ItemUpdateDto item) {
        log.info("==> Item update {} start", item);
        item.setId(itemId);
        item.setUserId(userId);
        ItemDtoResponse dtoResponse = itemService.update(item);
        log.info("<== Item updated {} complete", item);
        return dtoResponse;
    }

    @DeleteMapping("/{itemId}")
    public void delete(
            @RequestHeader(USER_ID_HEADER) long userId,
            @PathVariable long itemId) {
        log.info("==> Item delete userId {}, itemId {} start", userId, itemId);
        itemService.delete(userId, itemId);
        log.info("<== Item deleted userId {}, itemId {} complete", userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoResponse get(
            @RequestHeader(USER_ID_HEADER) long userId,
            @PathVariable long itemId) {
        log.info("==> Item get userId {}, itemId {} start", userId, itemId);
        ItemDtoResponse dtoResponse = itemService.get(userId, itemId);
        log.info("<== Item get userId {}, itemId {} complete", userId, itemId);
        return dtoResponse;
    }

    @GetMapping("/search")
    public List<ItemDtoResponse> search(@RequestParam(defaultValue = "") String text) {
        log.info("==> Item search text {} start", text);
        List<ItemDtoResponse> itemDtoResponses = itemService.search(text);
        log.info("<== Item search text {} complete", text);
        return itemDtoResponses;
    }
}
