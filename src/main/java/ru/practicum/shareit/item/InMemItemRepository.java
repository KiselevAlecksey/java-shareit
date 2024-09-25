package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class InMemItemRepository implements ItemRepository {
    private long countId = 0;

    private final Map<Long, Map<Long, Item>> itemBookingMap = new HashMap<>();;

    private final Map<Long, Map<Long, Item>> itemMap = new HashMap<>();;

    @Override
    public List<Item> getAll(long userId) {
        return itemMap.get(userId).values().stream()
                .peek(item -> {
                    if (item == null) throw new NotFoundException("Предмет не найден");
                })
                .toList();
    }

    @Override
    public Item saveBookingItem(long ownerId, long id, Item item) {

        Map<Long, Item> map = itemMap.get(ownerId);

        map.put(id, item);

        itemBookingMap.put(ownerId, map);

        delete(ownerId, id);

        return getBookingItem(ownerId, id).orElseThrow(() -> new NotFoundException("Преедмет не найден"));
    }

    @Override
    public boolean deleteBookingItem(long ownerId, long id, Item item) {

        Map<Long, Item> mapBooking = itemBookingMap.get(ownerId);

        Map<Long, Item> mapItems = itemMap.get(ownerId);

        mapBooking.putAll(mapItems);

        itemMap.remove(ownerId);

        itemMap.put(ownerId, mapBooking);

        mapBooking.remove(id);

        itemBookingMap.put(ownerId, mapBooking);

        return itemMap.get(ownerId).get(id) != null && getBookingItem(ownerId, id).isEmpty();

    }

    @Override
    public Item updateBookingItem(long ownerId, long id, Item item) {

        if (itemMap.get(ownerId) == null || itemMap.get(ownerId).get(id) == null) {
            throw new NotFoundException("Не удалось обновить предмет");
        }

        Map<Long, Item> mapBooking = itemBookingMap.get(ownerId);

        mapBooking.put(id, item);

        itemBookingMap.put(ownerId, mapBooking);

        return getBookingItem(ownerId, id).orElseThrow(() -> new NotFoundException("Предмет не найден"));
    }

    @Override
    public Item save(long userId, Item item) {

        boolean isExist = itemMap.values().stream()
                .flatMap(map -> map.values().stream())
                .anyMatch(value -> value.equals(item));

        if (isExist) {
            throw new NotFoundException("Не удалось сохранить предмет");
        }

        Item saveItem = item.toBuilder()
                        .id(++countId)
                                .ownerId(userId)
                                        .build();

        Map<Long, Item> map = itemMap.computeIfAbsent(userId, v -> new HashMap<>());

        map.put(countId, saveItem);

        itemMap.put(userId, map);

        return get(userId, saveItem.getId()).orElseThrow(() -> new NotFoundException("Предмет не найден"));
    }

    @Override
    public Item update(long userId, long itemId, Item item) {

        if (itemMap.get(userId) == null || itemMap.get(userId).get(itemId) == null) {
            throw new NotFoundException("Не удалось обновить предмет");
        }

        Map<Long, Item> map = itemMap.get(userId);

        map.put(itemId, item);

        itemMap.put(userId, map);

        return get(userId, itemId).orElseThrow(() -> new NotFoundException("Предмет не найден"));
    }

    @Override
    public void delete(long userId, long itemId) {
        itemMap.get(userId).remove(itemId);

        if (itemMap.get(userId).isEmpty()) {
            itemMap.remove(userId);
        }
    }

    @Override
    public Optional<Item> get(long userId, long itemId) {
        Map<Long, Item> map = itemMap.get(userId);

        if (map == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        Item item = map.get(itemId);

        return Optional.ofNullable(item);
    }

    @Override
    public List<Item> search(String text) {

        if (text.isEmpty()) return Collections.emptyList();

        // алгоритм поиска плохой
        List<Item> items = itemMap.values().stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .toList();

        return getSearchedItems(items, text);
    }

    private static List<Item> getSearchedItems(List<Item> items, String text) {

        String textLowerCase = text.toLowerCase();

        List<Item> searchedItems = items.stream().filter(item -> {

            if (!item.getAvailable()) return false;

            String name = item.getName().toLowerCase();

            String contentCompare = "";

            if (textLowerCase.length() > name.length()) {
                contentCompare = textLowerCase.substring(0, name.length() - 1);
            } else {
                contentCompare = textLowerCase;
            }

            int length = item.getName().length();
            int end = length;

            for (int i = 0; (end - 1 < length); i++, end++) {

                String nameCompare = name.substring(i, end);

                if (nameCompare.equals(contentCompare)) return true;
            }
            return false;
        })
                .toList();
        return searchedItems;
    }

    private Optional<Item> getBookingItem(long userId, long itemId) {
        Map<Long, Item> map = itemBookingMap.get(userId);

        if (map == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        Item item = map.get(itemId);

        return Optional.ofNullable(item);
    }
}
