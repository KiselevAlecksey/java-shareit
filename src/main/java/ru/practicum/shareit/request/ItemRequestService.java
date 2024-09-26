package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;

import java.util.Collection;

public interface ItemRequestService {

    Collection<ItemRequestDtoResponse> getAll(long userId);

    ItemRequestDtoResponse add(Long userId, ItemRequestDto item);

    ItemRequestDtoResponse update(Long userId, Long itemId, ItemRequestDto item);

    void delete(long userId, long itemId);

    ItemRequestDtoResponse get(long userId, long itemId);

    Collection<ItemRequestDtoResponse> search(String text);
}
