package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {

    Collection<Item> getAll(long userId);

    Optional<Item> save(Long userId, Item item);

    Optional<Item> update(Long userId, Long itemId, Item item);

    void delete(long userId, long itemId);

    Optional<Item> get(Long userId, Long itemId);

    Collection<Item> search(String text);

    Optional<Item> saveBookingItem(Long ownerId, Long id, Item item);

    boolean deleteBookingItem(Long ownerId, Long id, Item item);

    Optional<Item> updateBookingItem(Long ownerId, Long id, Item item);
}
