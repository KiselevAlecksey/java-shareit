package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {

    public static Item mapToItem(NewItemRequest request) {

        return Item.builder()
                .name(request.getName())
                .description(request.getDescription())
                .available(request.getAvailable())
                .build();
    }

    public static ItemDtoResponse mapToItemDto(Item item) {
        ItemDtoResponse dto = new ItemDtoResponse();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        return dto;
    }

    public static Item updateItemFields(Item item, UpdateItemRequest request) {
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
