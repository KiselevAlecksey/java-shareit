package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ParameterNotValidException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestServiceImpl implements ItemRequestService {

    ItemRequestRepository itemRepository;

    UserRepository userRepository;

    ItemRequestMapper itemRequestMapper;

    @Override
    public Collection<ItemRequestDtoResponse> getAll(long userId) {
        return itemRepository.getAll(userId).stream()
                .map(itemRequestMapper::mapToItemDto)
                .toList();
    }

    @Override
    public ItemRequestDtoResponse add(Long userId, ItemRequestDto itemDto) {

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

        ItemRequest item = itemRequestMapper.mapToItem(itemDto);

        ItemRequest itemCreated = itemRepository.add(userId, item);

        return itemRequestMapper.mapToItemDto(itemCreated);
    }

    @Override
    public ItemRequestDtoResponse update(Long userId, Long itemId, ItemRequestDto itemDto) {

        if (userId == null) {
            throw new NotFoundException("Пользователь должен быть указан");
        }

        if (itemId == null) {
            throw new NotFoundException("Предмет должен быть указан");
        }

        if (itemDto == null) {
            throw new NotFoundException("Поля предмета должны быть указаны");
        }

        ItemRequest item = itemRepository.get(userId, itemId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));

        itemRequestMapper.updateItemFields(item, itemDto);

        ItemRequest itemUpdated = itemRepository.update(userId, itemId, item);

        return itemRequestMapper.mapToItemDto(itemUpdated);
    }

    @Override
    public void delete(long userId, long itemId) {

        if (itemRepository.get(userId, itemId).isEmpty()) {
            throw new NotFoundException("Предмет не найден");
        }

        itemRepository.delete(userId, itemId);
    }

    @Override
    public ItemRequestDtoResponse get(long userId, long itemId) {
        ItemRequest item = itemRepository.get(userId, itemId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));

        return itemRequestMapper.mapToItemDto(item);
    }

    @Override
    public Collection<ItemRequestDtoResponse> search(String text) {
        return itemRepository.search(text).stream()
                .map(itemRequestMapper::mapToItemDto)
                .toList();
    }
}
