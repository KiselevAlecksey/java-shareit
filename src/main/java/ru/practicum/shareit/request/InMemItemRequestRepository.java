package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class InMemItemRequestRepository implements ItemRequestRepository {

    private long countId = 0;

    private final Map<Long, Map<Long, ItemRequest>> itemMap;

    @Override
    public Collection<ItemRequest> getAll(long userId) {
        return itemMap.get(userId).values().stream()
                .peek(item -> {
                    if (item == null) throw new NotFoundException("Предмет не найден");
                })
                .toList();
    }

    @Override
    public Optional<ItemRequest> add(long userId, ItemRequest item) {

        boolean isExist = itemMap.values().stream()
                .flatMap(map -> map.values().stream())
                .anyMatch(value -> value.equals(item));

        if (isExist) {
            throw new NotFoundException("Не удалось сохранить предмет");
        }

        ItemRequest saveItem = item.toBuilder()
                .id(++countId)
                .ownerId(userId)
                .build();

        Map<Long, ItemRequest> map = itemMap.computeIfAbsent(userId, v -> new HashMap<>());

        map.put(countId, saveItem);

        itemMap.put(userId, map);

        return getItem(userId, saveItem.getId());
    }

    @Override
    public Optional<ItemRequest> update(long userId, long itemId, ItemRequest item) {
        if (itemMap.get(userId) == null || itemMap.get(userId).get(itemId) == null) {
            throw new NotFoundException("Не удалось обновить предмет");
        }

        Map<Long, ItemRequest> map = itemMap.get(userId);

        map.put(itemId, item);

        itemMap.put(userId, map);

        return getItem(userId, itemId);
    }

    @Override
    public void delete(long userId, long itemId) {
        itemMap.get(userId).remove(itemId);

        if (itemMap.get(userId).isEmpty()) {
            itemMap.remove(userId);
        }
    }

    @Override
    public Optional<ItemRequest> get(long userId, long itemId) {
        return getItem(userId, itemId);
    }

    @Override
    public Collection<ItemRequest> search(String text) {

        if (text.isEmpty()) return new ArrayList<>();

        List<ItemRequest> items = itemMap.values().stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .toList();

        return getSearchedItems(items, text);
    }

    private static List<ItemRequest> getSearchedItems(List<ItemRequest> items, String text) {

        String textLowerCase = text.toLowerCase();

        List<ItemRequest> searchedItems = items.stream().filter(item -> {

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

    private Optional<ItemRequest> getItem(Long userId, Long itemId) {
        Map<Long, ItemRequest> map = itemMap.get(userId);

        if (map == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        ItemRequest item = map.get(itemId);

        if (item == null) {
            throw new NotFoundException("Предмет не найден");
        }

        return Optional.of(item);
    }
}
