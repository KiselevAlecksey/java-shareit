package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.Collection;

public interface ItemService {

    Collection<ItemDtoResponse> getAll(long userId);

    ItemDtoResponse add(Long userId, NewItemRequest item);

    ItemDtoResponse update(Long userId, Long itemId, UpdateItemRequest item);

    void delete(long userId, long itemId);

    ItemDtoResponse get(long userId, long itemId);

    Collection<ItemDtoResponse> search(String text);
}
