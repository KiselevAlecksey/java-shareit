package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public final class ItemMapper {
    final CommentMapper commentMapper;

    public Item mapToItem(ItemCreateDto request) {

        return Item.builder()
                .owner(User.builder().id(request.getUserId()).build())
                .name(request.getName())
                .description(request.getDescription())
                .available(request.getAvailable())
                .build();
    }

    public ItemResponseDto mapToItemDto(Item item) {

        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null,
                null,
                Collections.emptyList()
        );
    }

    public Item updateItemFields(Item item, ItemUpdateDto request) {
        if (request.hasName()) {
            item.setName(request.getName());
        }
        if (request.hasDescription()) {
            item.setDescription(request.getDescription());
        }
        if (request.hasAvailable()) {
            item.setAvailable(request.getAvailable());
        }
        return item;
    }
}
