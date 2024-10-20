package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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

    public ItemResponseDto mapToItemDto(Item item, LocalDateTime lastBooking, LocalDateTime nextBooking) {

        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                convertDateFormat(lastBooking),
                convertDateFormat(nextBooking),
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

    private String convertDateFormat(LocalDateTime localDateTime) {
        String dateTimeformatted = null;

        if (localDateTime != null) {
            dateTimeformatted = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                    .withZone(ZoneOffset.UTC)
                    .format(localDateTime);
        }
        return dateTimeformatted;
    }
}
