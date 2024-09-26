package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemItemRepository implements ItemRepository {
    private long countId = 0;

    private final Map<Long, Map<Long, Item>> itemBookingMap = new HashMap<>();

    private final Map<Long, Map<Long, Item>> itemsMap = new HashMap<>();

    private final Map<Long, List<Item>> userItemsMap = new HashMap<>();

    @Override
    public List<Item> getAll(long userId) {
        return userItemsMap.getOrDefault(userId, Collections.emptyList());
    }

    @Override
    public Item saveBookingItem(Item item) {

        long ownerId = item.getOwner().getId();

        long id = item.getId();

        Map<Long, Item> map = itemsMap.get(ownerId);

        map.put(id, item);

        itemBookingMap.put(ownerId, map);

        delete(ownerId, id);

        return getBookingItem(ownerId, id)
                .orElseThrow(() -> new NotFoundException("Преедмет не найден"));
    }

    @Override
    public boolean deleteBookingItem(Item item) {

        long ownerId = item.getOwner().getId();

        long id = item.getId();

        Map<Long, Item> mapBooking = itemBookingMap.get(ownerId);

        Map<Long, Item> mapItems = itemsMap.get(ownerId);

        mapBooking.putAll(mapItems);

        itemsMap.remove(ownerId);

        itemsMap.put(ownerId, mapBooking);

        mapBooking.remove(id);

        itemBookingMap.put(ownerId, mapBooking);

        return itemsMap.get(ownerId).get(id) != null && getBookingItem(ownerId, id).isEmpty();

    }

    @Override
    public Item updateBookingItem(Item item) {

        long ownerId = item.getOwner().getId();

        long id = item.getId();

        if (itemsMap.get(ownerId) == null || itemsMap.get(ownerId).get(id) == null) {
            throw new NotFoundException("Не удалось обновить предмет");
        }

        Map<Long, Item> mapBooking = itemBookingMap.get(ownerId);

        mapBooking.put(id, item);

        itemBookingMap.put(ownerId, mapBooking);

        return getBookingItem(ownerId, id)
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));
    }

    @Override
    public Item save(Item item) {

        boolean isExist = itemsMap.values().stream()
                .flatMap(map -> map.values().stream())
                .anyMatch(value -> value.equals(item));

        if (isExist) {
            throw new NotFoundException("Не удалось сохранить предмет");
        }

        User user = item.getOwner();

        Item saveItem = item.toBuilder()
                        .id(++countId)
                                .owner(user)
                                        .build();

        Map<Long, Item> map = itemsMap.computeIfAbsent(user.getId(), v -> new HashMap<>());

        map.put(countId, saveItem);

        itemsMap.put(user.getId(), map);

        userItemsMap.computeIfAbsent(user.getId(), k -> new ArrayList<>()).add(saveItem);

        return get(user.getId(), saveItem.getId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));
    }

    @Override
    public Item update(Item item) {

        long ownerId = item.getOwner().getId();

        long itemId = item.getId();

        if (itemsMap.get(ownerId) == null || itemsMap.get(ownerId).get(itemId) == null) {
            throw new NotFoundException("Не удалось обновить предмет");
        }

        Map<Long, Item> map = itemsMap.get(ownerId);

        map.put(ownerId, item);

        itemsMap.put(ownerId, map);

        List<Item> itemList = userItemsMap.get(ownerId);

        itemList.removeIf(item1 -> item1.getId().equals(itemId));

        itemList.add(item);

        return get(ownerId, item.getId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));
    }

    @Override
    public void delete(long userId, long itemId) {
        itemsMap.get(userId).remove(itemId);

        List<Item> itemList = userItemsMap.get(userId);

        itemList.removeIf(item1 -> item1.getId().equals(itemId));

        if (itemsMap.get(userId).isEmpty()) {
            itemsMap.remove(userId);
        }
    }

    @Override
    public Optional<Item> get(long userId, long itemId) {
        Map<Long, Item> map = itemsMap.get(userId);

        if (map == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        Item item = map.get(itemId);

        return Optional.ofNullable(item);
    }

    @Override
    public List<Item> search(String text) {

        if (text.isEmpty()) return Collections.emptyList();

        List<Item> items = itemsMap.values().stream()
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
