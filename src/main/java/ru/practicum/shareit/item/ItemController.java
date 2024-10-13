package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

import static ru.practicum.shareit.util.Const.USER_ID_HEADER;

@Slf4j
@RestController
@Validated
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemResponseDto> getAll(@RequestHeader(USER_ID_HEADER) long userId) {
        log.info("==> Users get all start");
        List<ItemResponseDto> items = itemService.getAll(userId);
        log.info("<== Users get all start");
        return items;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponseDto create(
            @RequestHeader(USER_ID_HEADER) long userId,
            @RequestBody @Valid ItemCreateDto item) {
        log.info("==> Item create {} start", item);
        item.setUserId(userId);
        ItemResponseDto dto = itemService.create(item);
        log.info("<== Item created {} complete", item);
        return dto;
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto update(
            @RequestHeader(USER_ID_HEADER) long ownerId,
            @PathVariable long itemId,
            @RequestBody @Valid ItemUpdateDto item) {
        log.info("==> Item update {} start", item);
        item.setId(itemId);
        item.setOwnerId(ownerId);
        ItemResponseDto dtoResponse = itemService.update(item);
        log.info("<== Item updated {} complete", item);
        return dtoResponse;
    }

    @DeleteMapping("/{itemId}")
    public void delete(
            @RequestHeader(USER_ID_HEADER) long userId,
            @PathVariable long itemId) {
        log.info("==> Item delete userId {}, itemId {} start", userId, itemId);
        itemService.delete(itemId);
        log.info("<== Item deleted userId {}, itemId {} complete", userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto get(
            @RequestHeader(USER_ID_HEADER) long userId,
            @PathVariable long itemId) {
        log.info("==> Item get userId {}, itemId {} start", userId, itemId);
        ItemResponseDto dtoResponse = itemService.get(userId, itemId);
        log.info("<== Item get userId {}, itemId {} complete", userId, itemId);
        return dtoResponse;
    }

    @GetMapping("/search")
    public List<ItemResponseDto> search(@RequestParam(defaultValue = "") String text) {
        log.info("==> Item search text {} start", text);
        List<ItemResponseDto> itemDtoResponses = itemService.search(text);
        log.info("<== Item search text {} complete", text);
        return itemDtoResponses;
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(
            @RequestHeader(USER_ID_HEADER) long userId,
            @PathVariable long itemId,
            @RequestBody @Valid CommentCreateDto comment) {
        log.info("==> Comment create {} start", comment);
        comment.setUserId(userId);
        comment.setItemId(itemId);
        CommentResponseDto dto = itemService.createComment(comment);
        log.info("<== Comment created {} complete", comment);
        return dto;
    }
}
