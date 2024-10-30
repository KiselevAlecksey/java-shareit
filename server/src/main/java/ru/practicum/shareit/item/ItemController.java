package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

import static ru.practicum.shareit.util.Const.USER_ID_HEADER;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemResponseDto> getAll(@RequestHeader(USER_ID_HEADER) long userId) {
        List<ItemResponseDto> items = itemService.getAll(userId);
        return items;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponseDto create(@RequestBody ItemCreateDto item) {
        ItemResponseDto dto = itemService.create(item);
        return dto;
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto update(@RequestBody ItemUpdateDto item) {
        ItemResponseDto dtoResponse = itemService.update(item);
        return dtoResponse;
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable long itemId) {
        itemService.delete(itemId);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto get(@PathVariable long itemId) {
        ItemResponseDto dtoResponse = itemService.get(itemId);
        return dtoResponse;
    }

    @GetMapping("/search")
    public List<ItemResponseDto> search(@RequestParam(defaultValue = "") String text) {
        List<ItemResponseDto> itemDtoResponses = itemService.search(text);
        return itemDtoResponses;
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@RequestBody CommentCreateDto comment) {
        CommentResponseDto dto = itemService.createComment(comment);
        return dto;
    }
}
