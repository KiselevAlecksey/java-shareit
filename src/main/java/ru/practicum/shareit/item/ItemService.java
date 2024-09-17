package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;

import java.util.Collection;

public interface ItemService {

    Collection<ItemDtoResponse> getAll(long userId);

    ItemDtoResponse add(Long userId, ItemDto item);

    ItemDtoResponse update(Long userId, Long itemId, ItemDto item);

    void delete(long userId, long itemId);

    ItemDtoResponse get(long userId, long itemId);

    Collection<ItemDtoResponse> search(String text);
}
