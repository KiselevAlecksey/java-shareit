package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {

    List<ItemResponseDto> getAll(long userId);

    ItemResponseDto create(ItemCreateDto item);

    ItemResponseDto update(ItemUpdateDto item);

    void delete(long itemId);

    ItemResponseDto get(long userId, long itemId);

    List<ItemResponseDto> search(String text);

    CommentResponseDto createComment(CommentCreateDto comment);
}
