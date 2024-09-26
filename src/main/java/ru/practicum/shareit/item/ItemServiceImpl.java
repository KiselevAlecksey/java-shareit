package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemServiceImpl implements ItemService {
    final ItemRepository itemRepository;

    final UserRepository userRepository;

    final ItemMapper itemMapper;

    @Override
    public List<ItemDtoResponse> getAll(long userId) {
        return itemRepository.getAll(userId).stream()
                .map(itemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public ItemDtoResponse create(ItemCreateDto itemDto) {

        Item item = itemMapper.mapToItem(itemDto);

        User user = userRepository.getById(item.getOwner().getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        item.getOwner().setEmail(user.getEmail());
        item.getOwner().setName(user.getName());

        Item itemCreated = itemRepository.save(item);

        return itemMapper.mapToItemDto(itemCreated);
    }

    @Override
    public ItemDtoResponse update(ItemUpdateDto itemDto) {

        Item item = itemRepository.get(itemDto.getUserId(), itemDto.getId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));

        itemMapper.updateItemFields(item, itemDto);

        Item itemUpdated = itemRepository.update(item);

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
