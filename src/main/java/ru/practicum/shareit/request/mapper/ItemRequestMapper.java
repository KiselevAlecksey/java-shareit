package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.dto.UpdateItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemRequestMapper {

    public static ItemRequest mapToItem(NewItemRequestDto request) {

        return ItemRequest.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public static ItemRequest mapToItem(ItemRequestDto request) {

        return ItemRequest.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public static ItemRequestDto mapToItemDto(ItemRequest item) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        return dto;
    }

    public static ItemRequest updateItemFields(ItemRequest item, UpdateItemRequestDto request) {
        if (request.hasName()) {
            item.setName(request.getName());
        }
        if (request.hasDescription()) {
            item.setDescription(request.getDescription());
        }
        return item;
    }
}
