package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    List<Item> getAll(long userId);

    Item save(Item item);

    Item update(Item item);

    void delete(long userId, long itemId);

    Optional<Item> get(long userId, long itemId);

    List<Item> search(String text);

    Item saveBookingItem(Item item);

    boolean deleteBookingItem(Item item);

    Item updateBookingItem(Item item);
}
