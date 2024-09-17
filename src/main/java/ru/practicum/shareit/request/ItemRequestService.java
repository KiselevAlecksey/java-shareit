package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.dto.UpdateItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {

    Collection<ItemRequestDto> getAll(long userId);

    ItemRequestDto add(Long userId, NewItemRequestDto item);

    ItemRequestDto update(Long userId, Long itemId, UpdateItemRequestDto item);

    void delete(long userId, long itemId);

    ItemRequestDto get(long userId, long itemId);

    Collection<ItemRequestDto> search(String text);
}
