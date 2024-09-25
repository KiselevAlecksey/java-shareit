package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ParameterNotValidException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemServiceImpl implements ItemService {
    final ItemRepository itemRepository;

    final ItemMapper itemMapper;

    @Override
    public List<ItemDtoResponse> getAll(long userId) {
        return itemRepository.getAll(userId).stream()
                .map(itemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public ItemDtoResponse add(long userId, ItemDto itemDto) {

        Item item = itemMapper.mapToItem(itemDto);

        Item itemCreated = itemRepository.save(userId, item);

        return itemMapper.mapToItemDto(itemCreated);
    }

    @Override
    public ItemDtoResponse update(long userId, long itemId, ItemDto itemDto) {

        Item item = itemRepository.get(userId, itemId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));

        itemMapper.updateItemFields(item, itemDto);

        Item itemUpdated = itemRepository.update(userId, itemId, item);
        return itemMapper.mapToItemDto(itemUpdated);
    }

    @Override
    public void delete(long userId, long itemId) {

        if (itemRepository.get(userId, itemId).isEmpty()) {
            throw new NotFoundException("Предмет не найден");
        }

        itemRepository.delete(userId, itemId);
    }

    @Override
    public ItemDtoResponse get(long userId, long itemId) {
        Item item = itemRepository.get(userId, itemId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));

        return itemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemDtoResponse> search(String text) {
        return itemRepository.search(text).stream()
                .map(itemMapper::mapToItemDto)
                .toList();
    }
}
