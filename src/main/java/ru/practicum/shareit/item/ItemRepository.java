package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    List<Item> getAll(long userId);

    Item save(long userId, Item item);

    Item update(long userId, long itemId, Item item);

    void delete(long userId, long itemId);

    Optional<Item> get(long userId, long itemId);

    List<Item> search(String text);

    Item saveBookingItem(long ownerId, long id, Item item);

    boolean deleteBookingItem(long ownerId, long id, Item item);

    Item updateBookingItem(long ownerId, long id, Item item);
}
