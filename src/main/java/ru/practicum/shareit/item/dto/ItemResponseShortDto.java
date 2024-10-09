package ru.practicum.shareit.item.dto;

import java.util.List;

public record ItemResponseShortDto(
        Long id,

        String name,

        String description,

        Boolean available,

        List<CommentResponseDto> comments) {
}
