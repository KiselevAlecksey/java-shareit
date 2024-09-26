package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {

    List<ItemDtoResponse> getAll(long userId);

    ItemDtoResponse create(ItemCreateDto item);

    ItemDtoResponse update(ItemUpdateDto item);

    void delete(long userId, long itemId);

    ItemDtoResponse get(long userId, long itemId);

    List<ItemDtoResponse> search(String text);
}
