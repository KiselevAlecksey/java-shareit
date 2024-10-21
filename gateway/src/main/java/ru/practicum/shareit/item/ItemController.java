package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import static ru.practicum.shareit.util.Const.USER_ID_HEADER;

@Slf4j
@RestController
@Validated
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(USER_ID_HEADER) long userId) {
        log.info("==> Users get all start");
        ResponseEntity<Object> items = itemClient.getAll(userId);
        log.info("<== Users get all start");
        return items;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(
            @RequestHeader(USER_ID_HEADER) long userId,
            @RequestBody @Validated ItemCreateDto item) {
        log.info("==> Item create {} start", item);
        item.setUserId(userId);
        ResponseEntity<Object> dto = itemClient.create(item);
        log.info("<== Item created {} complete", dto);
        return dto;
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(
            @RequestHeader(USER_ID_HEADER) long ownerId,
            @PathVariable long itemId,
            @RequestBody @Validated ItemUpdateDto item) {
        log.info("==> Item update {} start", item);
        item.setId(itemId);
        item.setOwnerId(ownerId);
        ResponseEntity<Object> dtoResponse = itemClient.update(item);
        log.info("<== Item updated {} complete", dtoResponse);
        return dtoResponse;
    }

    @DeleteMapping("/{itemId}")
    public void delete(
            @RequestHeader(USER_ID_HEADER) long userId,
            @PathVariable long itemId) {
        log.info("==> Item delete userId {}, itemId {} start", userId, itemId);
        itemClient.delete(itemId);
        log.info("<== Item deleted userId {}, itemId {} complete", userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(
            @RequestHeader(USER_ID_HEADER) long ownerId,
            @PathVariable long itemId) {
        log.info("==> Item get userId {}, itemId {} start", ownerId, itemId);
        ResponseEntity<Object> dtoResponse = itemClient.get(itemId);
        log.info("<== Item get userId {}, itemId {} complete", ownerId, itemId);
        return dtoResponse;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestHeader(USER_ID_HEADER) long userId,
            @RequestParam(defaultValue = "") String text) {
        log.info("==> Item search text {} start", text);
        ResponseEntity<Object> itemDtoResponses = itemClient.search(userId, text);
        log.info("<== Item search text {} complete", text);
        return itemDtoResponses;
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createComment(
            @RequestHeader(USER_ID_HEADER) long userId,
            @PathVariable long itemId,
            @RequestBody @Validated CommentCreateDto comment) {
        log.info("==> Comment create {} start", comment);
        comment.setUserId(userId);
        comment.setItemId(itemId);
        ResponseEntity<Object> dto = itemClient.createComment(comment);
        log.info("<== Comment created {} complete", dto);
        return dto;
    }
}
