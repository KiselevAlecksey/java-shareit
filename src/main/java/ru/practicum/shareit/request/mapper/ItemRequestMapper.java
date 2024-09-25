package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.model.ItemRequest;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemRequestMapper {

    public ItemRequest mapToItem(ItemRequestDto request) {

        return ItemRequest.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public ItemRequestDtoResponse mapToItemDto(ItemRequest item) {
        return new ItemRequestDtoResponse(
                item.getId(),
                item.getName(),
                item.getDescription()
        );
    }

    public ItemRequest updateItemFields(ItemRequest item, ItemRequestDto request) {
        if (request.hasName()) {
            item.setName(request.getName());
        }
        if (request.hasDescription()) {
            item.setDescription(request.getDescription());
        }
        return item;
    }
}
