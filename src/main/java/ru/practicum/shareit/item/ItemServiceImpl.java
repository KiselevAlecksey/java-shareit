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

import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemServiceImpl implements ItemService {
    final ItemRepository itemRepository;

    final UserRepository userRepository;

    @Override
    public Collection<ItemDtoResponse> getAll(long userId) {
        return itemRepository.getAll(userId).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public ItemDtoResponse add(Long userId, ItemDto itemDto) {

        if (userId == null || userRepository.getById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }

        if (itemDto == null) {
            throw new NotFoundException("Невозможно сохранить пустой предмет");
        }

        if (itemDto.getAvailable() == null) {
            throw new ParameterNotValidException("available", "Статус должен быть указан");
        }

        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
            throw new ParameterNotValidException("name", "Имя должно быть указано");
        }

        if (itemDto.getDescription() == null || itemDto.getDescription().isEmpty()) {
            throw new ParameterNotValidException("description", "Описание должно быть указано");
        }

        Item item = ItemMapper.mapToItem(itemDto);

        Item itemCreated = itemRepository.save(userId, item).orElseThrow(
                () -> new NotFoundException("Предмет не найден")
        );

        return ItemMapper.mapToItemDto(itemCreated);
    }

    @Override
    public ItemDtoResponse update(Long userId, Long itemId, ItemDto itemDto) {

        if (userId == null) {
            throw new NotFoundException("Пользователь должен быть указан");
        }

        if (itemId == null) {
            throw new NotFoundException("Предмет должен быть указан");
        }

        if (itemDto == null) {
            throw new NotFoundException("Поля предмета должны быть указаны");
        }

        Item item = itemRepository.get(userId, itemId).orElseThrow(
                () -> new NotFoundException("Предмет не найден")
        );

        ItemMapper.updateItemFields(item, itemDto);

        Item itemUpdated = itemRepository.update(userId, itemId, item).orElseThrow(
                () -> new NotFoundException("Предмет не найден")
        );

        return ItemMapper.mapToItemDto(itemUpdated);
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
        Item item = itemRepository.get(userId, itemId).orElseThrow(
                () -> new NotFoundException("Предмет не найден")
        );

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public Collection<ItemDtoResponse> search(String text) {
        return itemRepository.search(text).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }
}
