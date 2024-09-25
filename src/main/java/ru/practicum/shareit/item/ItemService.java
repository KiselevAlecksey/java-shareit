package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    List<ItemDtoResponse> getAll(long userId);

    ItemDtoResponse add(long userId, ItemDto item);

    ItemDtoResponse update(long userId, long itemId, ItemDto item);

    void delete(long userId, long itemId);

    ItemDtoResponse get(long userId, long itemId);

    List<ItemDtoResponse> search(String text);
}
