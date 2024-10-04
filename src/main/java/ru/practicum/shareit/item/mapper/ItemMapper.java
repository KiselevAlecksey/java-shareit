package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

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

        List<CommentResponseDto> responseDtoList = Collections.emptyList();

        if (item.getComments() != null && !item.getComments().isEmpty()) {
            responseDtoList = item.getComments().stream()
                    .map(commentMapper::mapToCommentDto)
                    .toList();
        }

        String last = null;

        if (item.getLastBooking() != null) {
            last = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                    .withZone(ZoneOffset.UTC)
                    .format(item.getLastBooking());
        }

        String next = null;

        if (item.getNextBooking() != null) {
            next = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                    .withZone(ZoneOffset.UTC)
                    .format(item.getNextBooking());
        }

        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                last,
                next,
                responseDtoList
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
