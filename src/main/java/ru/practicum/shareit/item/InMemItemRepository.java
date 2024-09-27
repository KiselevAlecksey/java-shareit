package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemItemRepository implements ItemRepository {
    private long countId = 0;

    private final Map<Long, Set<Item>> itemBookingMap = new HashMap<>();

    private final Map<Long, Set<Item>> itemsByUserMap = new HashMap<>();

    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public List<Item> getAll(long userId) {
        return itemsByUserMap.getOrDefault(userId, Collections.emptySet()).stream().toList();
    }

    @Override
    public Item saveBookingItem(Item item) {

        long ownerId = item.getOwner().getId();

        long id = item.getId();

        Set<Item> itemSet = itemsByUserMap.get(ownerId);

        itemSet.add(item);

        itemBookingMap.put(ownerId, itemSet);

        delete(ownerId, id);

        return getBookingItem(ownerId, id)
                .orElseThrow(() -> new NotFoundException("Преедмет не найден"));
    }

    @Override
    public boolean deleteBookingItem(Item item) {

        long ownerId = item.getOwner().getId();

        long id = item.getId();

        Item itemDeleted = items.get(id);

        Set<Item> mapBooking = itemBookingMap.get(ownerId);

        Set<Item> mapItems = itemsByUserMap.get(ownerId);

        mapBooking.addAll(mapItems);

        itemsByUserMap.remove(ownerId);

        itemsByUserMap.put(ownerId, mapBooking);

        mapBooking.remove(itemDeleted);

        itemBookingMap.put(ownerId, mapBooking);

        return !itemsByUserMap.get(ownerId).contains(itemDeleted) && getBookingItem(ownerId, id).isEmpty();

    }

    @Override
    public Item updateBookingItem(Item item) {

        long ownerId = item.getOwner().getId();

        long id = item.getId();

        if (itemsByUserMap.get(ownerId) == null || !itemsByUserMap.get(ownerId).contains(item)) {
            throw new NotFoundException("Не удалось обновить предмет");
        }

        Set<Item> mapBooking = itemBookingMap.get(ownerId);

        mapBooking.add(item);

        itemBookingMap.put(ownerId, mapBooking);

        return getBookingItem(ownerId, id)
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));
    }

    @Override
    public Item save(Item item) {

        User user = item.getOwner();

        Item saveItem = item.toBuilder()
                        .id(++countId)
                                .owner(user)
                                        .build();

        Set<Item> itemSet = itemsByUserMap.computeIfAbsent(user.getId(), v -> new HashSet<>());

        itemSet.add(saveItem);

        itemsByUserMap.put(user.getId(), itemSet);

        items.put(saveItem.getId(), saveItem);

        return get(user.getId(), saveItem.getId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));
    }

    @Override
    public Item update(Item item) {

        long ownerId = item.getOwner().getId();

        long itemId = item.getId();

        Set<Item> itemSet = itemsByUserMap.get(ownerId);

        Item item1 = items.get(itemId);

        if (itemsByUserMap.get(ownerId) == null || !itemSet.contains(item1)) {
            throw new NotFoundException("Не удалось обновить предмет");
        }

        itemSet.add(item);

        itemsByUserMap.put(ownerId, itemSet);

        items.put(itemId, item);

        return get(ownerId, item.getId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));
    }

    @Override
    public void delete(long userId, long itemId) {
        itemsByUserMap.get(userId).remove(items.get(itemId));

        items.remove(itemId);

        if (itemsByUserMap.get(userId).isEmpty()) {
            itemsByUserMap.remove(userId);
        }
    }

    @Override
    public Optional<Item> get(long userId, long itemId) {
        Set<Item> itemSet = itemsByUserMap.get(userId);

        if (itemSet == null) {
            throw new NotFoundException("Предмет не найден");
        }

        Item item = items.get(itemId);

        return Optional.ofNullable(item);
    }

    @Override
    public List<Item> search(String text) {

        if (text.isEmpty()) return Collections.emptyList();

        List<Item> items = itemsByUserMap.values().stream()
                .flatMap(Set::stream)
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
        Set<Item> itemSet = itemBookingMap.get(userId);

        if (itemSet == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        Item item = items.get(itemId);

        return Optional.ofNullable(item);
    }
}
