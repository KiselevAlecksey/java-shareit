package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ParameterNotValidException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.dto.UpdateItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestServiceImpl implements ItemRequestService {

    final ItemRequestRepository itemRepository;

    final UserRepository userRepository;

    @Override
    public Collection<ItemRequestDto> getAll(long userId) {
        return itemRepository.getAll(userId).stream()
                .map(ItemRequestMapper::mapToItemDto)
                .toList();
    }

    @Override
    public ItemRequestDto add(Long userId, NewItemRequestDto itemDto) {

        if (userId == null || userRepository.getById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }

        if (itemDto == null) {
            throw new NotFoundException("Невозможно сохранить пустой предмет");
        }

        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
            throw new ParameterNotValidException("name", "Имя должно быть указано");
        }

        if (itemDto.getDescription() == null || itemDto.getDescription().isEmpty()) {
            throw new ParameterNotValidException("description", "Описание должно быть указано");
        }

        ItemRequest item = ItemRequestMapper.mapToItem(itemDto);

        ItemRequest itemCreated = itemRepository.add(userId, item).orElseThrow(
                () -> new NotFoundException("Предмет не найден")
        );

        return ItemRequestMapper.mapToItemDto(itemCreated);
    }

    @Override
    public ItemRequestDto update(Long userId, Long itemId, UpdateItemRequestDto itemDto) {

        if (userId == null) {
            throw new NotFoundException("Пользователь должен быть указан");
        }

        if (itemId == null) {
            throw new NotFoundException("Предмет должен быть указан");
        }

        if (itemDto == null) {
            throw new NotFoundException("Поля предмета должны быть указаны");
        }

        ItemRequest item = itemRepository.get(userId, itemId).orElseThrow(
                () -> new NotFoundException("Предмет не найден")
        );

        ItemRequestMapper.updateItemFields(item, itemDto);

        ItemRequest itemUpdated = itemRepository.update(userId, itemId, item).orElseThrow(
                () -> new NotFoundException("Предмет не найден")
        );

        return ItemRequestMapper.mapToItemDto(itemUpdated);
    }

    @Override
    public void delete(long userId, long itemId) {

        if (itemRepository.get(userId, itemId).isEmpty()) {
            throw new NotFoundException("Предмет не найден");
        }

        itemRepository.delete(userId, itemId);
    }

    @Override
    public ItemRequestDto get(long userId, long itemId) {
        ItemRequest item = itemRepository.get(userId, itemId).orElseThrow(
                () -> new NotFoundException("Предмет не найден")
        );

        return ItemRequestMapper.mapToItemDto(item);
    }

    @Override
    public Collection<ItemRequestDto> search(String text) {
        return itemRepository.search(text).stream()
                .map(ItemRequestMapper::mapToItemDto)
                .toList();
    }
}
