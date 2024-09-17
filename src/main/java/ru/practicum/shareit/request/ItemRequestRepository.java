package ru.practicum.shareit.request;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;
import java.util.Optional;

public interface ItemRequestRepository {

    Collection<ItemRequest> getAll(long userId);

    ItemRequest add(long userId, ItemRequest item);

    ItemRequest update(long userId, long itemId, ItemRequest item);

    void delete(long userId, long itemId);

    Optional<ItemRequest> get(long userId, long itemId);

    Collection<ItemRequest> search(String text);
}
